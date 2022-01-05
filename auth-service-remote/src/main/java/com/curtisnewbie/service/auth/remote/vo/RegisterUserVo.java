package com.curtisnewbie.service.auth.remote.vo;


import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Vo for registering new user
 *
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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
