package com.ccc.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ccc
 * @create 2023-11-13 23:04
 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String userPassword;
}
