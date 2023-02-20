package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateUserInfoReqVo {

    /**
     * User id
     */
    @NotNull
    private Integer id;

    /**
     * User Role No
     */
    private String roleNo;

    /**
     * is_disabled
     */
    private Integer isDisabled;
}
