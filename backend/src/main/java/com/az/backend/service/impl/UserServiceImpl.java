package com.az.backend.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.az.backend.pojo.domain.User;
import com.az.backend.service.UserService;
import com.az.backend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author az
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-26 21:52:34
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值 混淆密码
     */
    private static final String salt="az";

    /**
     * 用户登录态键
     */
    private static final String USER_LOGIN_STATE="userLoginState";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        if(userAccount.length()<4){
            return -2;
        }
        if(userPassword.length()<8||checkPassword.length()<8){
            return -3;
        }

        //检验特殊字符
        //
        if(!userPassword.equals(checkPassword)){
            return -4;
        }

        //账户不能重复
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count>1){
            return -5;
        }
        //2.加密密码
        String encodepassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        //3.插入数据
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encodepassword);
        int save = userMapper.insert(user);
        if(save==0){
            return -6;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if(userAccount.length()<4){
            return null;
        }
        if(userPassword.length()<8){
            return null;
        }

        //检验特殊字符
        //用户不存在
        String encodepassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount).eq("userPassword",encodepassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }
        //3.用户脱敏
        User newUser=new User();
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        newUser.setUserAccount(user.getUserAccount());
        newUser.setAvatarUrl(user.getAvatarUrl());
        newUser.setGender(user.getGender());
        newUser.setPhone(user.getPhone());
        newUser.setEmail(user.getEmail());
        newUser.setUserStatus(user.getUserStatus());
        newUser.setCreatTime(user.getCreatTime());
        //4.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,newUser);


        return newUser;
    }
}




