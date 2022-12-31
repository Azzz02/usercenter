package com.az.backend.service.impl;

import com.az.backend.constant.UserConstant;
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

        //用户不存在
        String encodepassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount).eq("userPassword",encodepassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            log.info("user login failed ,userAccount cannot match userPassword");
            return null;
        }
        //3.用户脱敏
        User newUser=getSafetyUser(user);
        //4.记录用户登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,newUser);

        return newUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        User safetyUser=new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreatTime(originUser.getCreatTime());
        return safetyUser;
    }

}




