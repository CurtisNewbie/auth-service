package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.service.auth.remote.consts.UserRole;
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

    /** role */
    private UserRole role;

    /** role no */
    private String roleNo;

    /** role name */
    private String roleName;

    /** when the user is registered */
    private String registerDate;
}
