package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.Data;
import lombok.ToString;

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
    private UserRole userRole;

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }
}
