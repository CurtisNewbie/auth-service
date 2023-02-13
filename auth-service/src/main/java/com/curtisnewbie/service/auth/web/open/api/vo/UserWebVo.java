package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@EqualsAndHashCode
@Data
public class UserWebVo {

    /** id */
    private Integer id;

    /**
     * username
     */
    private String username;

    /**
     * role
     */
    @Deprecated
    private UserRole role;

    /**
     * Role No
     */
    private String roleNo;

    /**
     * Services permitted
     */
    private List<String> services;
}
