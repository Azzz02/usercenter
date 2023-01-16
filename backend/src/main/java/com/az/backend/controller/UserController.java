package com.az.backend.controller;

import com.az.backend.constant.UserConstant;
import com.az.backend.pojo.domain.User;
import com.az.backend.pojo.domain.request.UserLoginRequest;
import com.az.backend.pojo.domain.request.UserRegisterRequest;
import com.az.backend.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null){
            return null;
        }
        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getUserPassword();
        String checkPassword=userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest==null){
            return null;
        }
        String userAccount=userLoginRequest.getUserAccount();
        String userPassword=userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }

        return userService.userLogin(userAccount, userPassword,request);
    }

    @GetMapping("/current")
    public User getCurrent(HttpServletRequest request){
        User currentUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(currentUser==null){
            return null;
        }
        long id=currentUser.getId();
        User user = userService.getById(id);
        return userService.getSafetyUser(user);
    }

    @GetMapping("/search")
    public List<User> searchUser(String username,HttpServletRequest request){
        //仅管理员可查询
        if(!isAdmin(request)){
            return new ArrayList<>();
        }

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){
        if(!isAdmin(request)){
            return false;
        }

        if(id<=0){
            return false;
        }

        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user=(User) attribute;
        if(user==null||user.getUserRole()!=UserConstant.ADMIN_ROLE){
            return false;
        }
        return true;
    }

}
