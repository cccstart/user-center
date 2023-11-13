package com.ccc.usercenter.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccc.usercenter.model.domain.User;
import com.ccc.usercenter.service.UserService;
import com.ccc.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

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

    /**
     * 用户登录状态键
     */
    private static final String USER_LOGIN_STATE = "userLoginState";

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
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验账号，密码，校验密码是否符合规则
        //非空
        if (StringUtils.isBlank(userAccount) &&
                StringUtils.isBlank(userPassword) &&
                StringUtils.isBlank(checkPassword)) {
            return -1;
        }
        //账号长度不小于4位
        if (userAccount.length() < 4) {
            return -1;
        }
        //密码长度不小于8位
        if (userPassword.length() < 8) {
            return -1;
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
            return -1;
        }

        //2.对密码进行加密，可以加盐
        String encodedPassword = DigestUtils.md5DigestAsHex((SALT + SALT + userPassword + SALT + SALT).getBytes());
        //3.插入数据库，返回用户id
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encodedPassword);
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
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());

        //4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }
}




