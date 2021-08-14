package com.curtisnewbie.auth.vo;

import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class RegisterUserWebVo {

    /**
     * username
     */
    private String username;

    /**
     * password (in plain text)
     */
    private String password;

    /**
     * User role
     */
    private String userRole;
}
