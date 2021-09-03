package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Basic info of user
 *
 * @author yongjie.zhuang
 */
@Data
public class UserInfoVo implements Serializable {

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

    /** when the user is created */
    private Date createTime;

    /** when the user is updated */
    private Date updateTime;

    /** who updated this user */
    private String updateBy;

    /** who created this user */
    private String createBy;
}
