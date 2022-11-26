package com.az.backend.service;
import java.util.Date;

import com.az.backend.pojo.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("az");
        user.setUserAccount("123");
        user.setAvatarUrl("https://cdn.acwing.com/media/user/profile/photo/84759_lg_83104530c6.jpg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);

    }

    @Test
    void userRegister() {
        String userAccount="azzz";
        String userPassword="12345678";
        String checkPassword="12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(2,result);
    }
}