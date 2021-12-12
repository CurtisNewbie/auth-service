package com.curtisnewbie.auth.vo;

import lombok.Data;

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
}
