package com.curtisnewbie.service.auth.remote.vo;


import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.Data;

import java.io.Serializable;

/**
 * Vo for registering new user
 *
 * @author yongjie.zhuang
 */
@Data
public class RegisterUserVo implements Serializable {

    /**
     * username
     */
    private String username;

    /**
     * password (in plain text)
     */
    private String password;

    /**
     * role
     */
    private UserRole role;

    /**
     * create by
     */
    private String createBy;
}
