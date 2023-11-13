package com.ccc.usercenter.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ccc.usercenter.model.domain.User;
import com.ccc.usercenter.model.domain.request.UserLoginRequest;
import com.ccc.usercenter.model.domain.request.UserRegisterRequest;
import com.ccc.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 * @author ccc
 * @create 2023-11-13 23:01
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isBlank(userAccount) && StringUtils.isBlank(userPassword) && StringUtils.isBlank(checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isBlank(userAccount) && StringUtils.isBlank(userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }
}
