package com.curtisnewbie.service.auth.remote.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
