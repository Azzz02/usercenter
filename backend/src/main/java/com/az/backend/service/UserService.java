package com.az.backend.service;

import com.az.backend.pojo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author az
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-26 21:52:34
*/
public interface UserService extends IService<User> {

    /**
     * 用户注释
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param request 存登录态
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount,String userPassword,HttpServletRequest request);

}
