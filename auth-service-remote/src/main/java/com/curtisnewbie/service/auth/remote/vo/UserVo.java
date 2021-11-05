package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User vo
 *
 * @author yongjie.zhuang
 */
@Data
public class UserVo implements Serializable {

    /** primary key */
    private Integer id;

    /** username (must be unique) */
    private String username;

    /** role */
    private String role;

    /** when the user is created */
    private LocalDateTime createTime;

    /** when the user is updated */
    private LocalDateTime updateTime;

    /** who updated this user */
    private String updateBy;

    /** who created this user */
    private String createBy;
}