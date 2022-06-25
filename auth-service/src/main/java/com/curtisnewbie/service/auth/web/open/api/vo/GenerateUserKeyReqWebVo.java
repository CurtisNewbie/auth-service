package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author yongjie.zhuang
 */
@Data
public class GenerateUserKeyReqWebVo {

    /** password */
    @NotBlank
    private String password;

    /** name of the key */
    @NotBlank
    private String keyName;

    @ToString.Include(name = "password")
    public String passwordMask() {
        return "****";
    }

}
