package com.ccc.usercenter.service.impl;

import com.ccc.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ccc
 * @create 2023-11-13 16:48
 */
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Test
    public void testRegister(){

        long l = userService.userRegister("", "12345678", "12345678","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("chang", "", "12345678","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("chang", "12345678", "","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("ccc", "12345678", "12345678","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("chang", "123456", "123456","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("chang", "12345678", "123456789","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("chang%%", "12345678", "12345678","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("chang%%", "12345678", "12345678","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
        l = userService.userRegister("123", "12345678", "12345678","1");
        Assertions.assertEquals(true,l<0);
        System.out.println(l);
//        l = userService.userRegister("chang", "12345678", "12345678");
//        Assertions.assertEquals(true,l>0);
//        System.out.println(l);
        l = userService.userRegister("chenchen", "12345678", "12345678","1");
        Assertions.assertEquals(true,l>0);
        System.out.println(l);

    }

}