package com.ccc.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccc.usercenter.common.ErrorCode;
import com.ccc.usercenter.exception.BusinessException;
import com.ccc.usercenter.model.domain.User;
import com.ccc.usercenter.service.UserService;
import com.ccc.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static com.ccc.usercenter.contant.Userconstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{

    /**
     * 密码加密的盐值
     */
    private static final String SALT = "ccc";



    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 检验密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.校验账号，密码，校验密码是否符合规则
        //非空
        if (StringUtils.isBlank(userAccount) &&
                StringUtils.isBlank(userPassword) &&
                StringUtils.isBlank(checkPassword) &&
                StringUtils.isBlank(planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //账号长度不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        //密码长度不小于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }

        if (planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }

        //密码和校验密码一致
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账号不能包含特殊字符
        final String REGEX = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        if (Pattern.compile(REGEX).matcher(userAccount).find()){
            return -1;
        }
        //账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long aLong = userMapper.selectCount(queryWrapper);
        if (aLong > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }

        //星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planet_code",planetCode);
        aLong = userMapper.selectCount(queryWrapper);
        if (aLong > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号重复");
        }

        //2.对密码进行加密，可以加盐
        String encodedPassword = DigestUtils.md5DigestAsHex((SALT + SALT + userPassword + SALT + SALT).getBytes());
        //3.插入数据库，返回用户id
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encodedPassword);
        user.setPlanetCode(planetCode);
        int insert = userMapper.insert(user);
        if (insert < 1) {
            return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验账号，密码是否合法
        //非空
        if (StringUtils.isBlank(userAccount) &&
                StringUtils.isBlank(userPassword)) {
            return null;
        }
        //账号长度不小于4位
        if (userAccount.length() < 4) {
            return null;
        }
        //密码长度不小于8位
        if (userPassword.length() < 8) {
            return null;
        }
        //账号不能包含特殊字符
        final String REGEX = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        if (Pattern.compile(REGEX).matcher(userAccount).find()){
            return null;
        }

        //2.查询用户是否存在
        //对密码进行加密，注意加盐
        String encodedPassword = DigestUtils.md5DigestAsHex((SALT + SALT + userPassword + SALT + SALT).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",encodedPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        //3.对用户信息脱敏
        User safetyUser = getSafetyUser(user);

        //4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




