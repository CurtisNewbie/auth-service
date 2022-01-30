package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yongjie.zhuang
 */
@Data
public class GenerateUserKeyReqWebVo {

    /** username */
    @NotBlank
    private String username;

    /** password */
    @NotBlank
    private String password;

}
