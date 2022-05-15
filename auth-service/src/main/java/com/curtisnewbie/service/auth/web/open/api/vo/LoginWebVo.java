package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

/**
 * @author yongjie.zhuang
 */
@Data
public class LoginWebVo {

    /** username */
    @NotBlank
    private String username;

    /** password */
    @NotBlank
    private String password;

    /** the service that sent the login request */
    @Nullable
    private String appName;

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }
}
