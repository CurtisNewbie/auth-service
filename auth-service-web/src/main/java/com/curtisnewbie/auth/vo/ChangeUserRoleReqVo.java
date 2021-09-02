package com.curtisnewbie.auth.vo;

import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class ChangeUserRoleReqVo {

    /**
     * User id
     */
    private Integer id;

    /**
     * User role
     */
    private String role;
}
