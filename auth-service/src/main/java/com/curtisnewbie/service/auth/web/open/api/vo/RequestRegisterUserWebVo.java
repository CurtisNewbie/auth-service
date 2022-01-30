package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author yongjie.zhuang
 */
@Data
public class RequestRegisterUserWebVo {

    /**
     * username
     */
    private String username;

    /**
     * password (in plain text)
     */
    private String password;

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }
}
