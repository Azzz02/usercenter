package com.az.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.az.backend.pojo.domain.User;
import com.az.backend.service.UserService;
import com.az.backend.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
* @author az
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-26 21:52:34
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

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
        final String salt="az";
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
}




