package com.az.backend.pojo.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -5171461214955190764L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
