package com.curtisnewbie.service.auth.remote.vo;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginVo {

    /** username */
    @NotBlank
    private String username;

    /** password */
    @NotBlank
    private String password;

    /** application name that the user is trying to use */
    @NotBlank
    private String appName;

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }
}
