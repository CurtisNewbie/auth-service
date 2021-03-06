package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateUserInfoReqVo {

    /**
     * User id
     */
    private Integer id;

    /**
     * User role
     */
    private String role;

    /**
     * is_disabled
     */
    private Integer isDisabled;
}
