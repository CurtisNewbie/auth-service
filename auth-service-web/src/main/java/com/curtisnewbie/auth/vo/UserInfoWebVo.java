package com.curtisnewbie.auth.vo;

import lombok.Data;

/**
 * UserInfo web vo
 *
 * @author yongjie.zhuang
 */
@Data
public class UserInfoWebVo {

    /** id */
    private Integer id;

    /**
     * username
     */
    private String username;

    /**
     * role
     */
    private String role;

    /** whether the user is disabled, 0-normal, 1-disabled */
    private Integer isDisabled;
}
