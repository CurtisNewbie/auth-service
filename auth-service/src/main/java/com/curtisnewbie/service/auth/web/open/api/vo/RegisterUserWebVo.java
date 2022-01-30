package com.curtisnewbie.service.auth.web.open.api.vo;

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
    private String userRole;

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }
}
