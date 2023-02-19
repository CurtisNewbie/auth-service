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

    private Integer id;
    private String username;
    private String roleName;
    private String roleNo;
    private String userNo;

    /** Services permitted */
    private List<String> services;

    /** will always be null */
    @Deprecated
    private UserRole role;
}
