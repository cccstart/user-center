package com.ccc.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccc.usercenter.model.domain.User;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 检验密码
     * @return
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);
}
