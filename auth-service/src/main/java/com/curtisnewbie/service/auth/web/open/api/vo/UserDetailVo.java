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

    /** username */
    private String username;

    /** role */
    private UserRole role;

    /** when the user is registered */
    private String registerDate;
}
