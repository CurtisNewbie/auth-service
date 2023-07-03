package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class UserDetailVo {

    /** id */
    private Integer id;

    /** user no */
    private String userNo;

    /** username */
    private String username;

    /** role no */
    private String roleNo;

    /** role name */
    private String roleName;

    /** when the user is registered */
    private String registerDate;
}
