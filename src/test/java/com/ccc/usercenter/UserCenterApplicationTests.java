package com.ccc.usercenter;
import java.util.Date;

import com.ccc.usercenter.service.UserService;
import com.ccc.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserCenterApplicationTests {
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUserName("ccc");
        user.setUserAccount("123");
        user.setAvatarUrl("http://fjisenr");
        user.setGender((byte) 0);
        user.setUserPassword("123");
        user.setPhone("4556");
        user.setEmail("789");

        boolean save = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(save);
    }

}
