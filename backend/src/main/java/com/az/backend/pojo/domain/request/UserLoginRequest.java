package com.az.backend.pojo.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -5171461214955190764L;

    private String userAccount;
    private String userPassword;
}
