package com.curtisnewbie.service.auth.dao;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * Join table between application and user
 * </p>
 *
 * @author yongjie.zhuang
 */
@Data
public class UserApp {
    /** primary key */
    private Integer id;

    /** user's id */
    private Integer userId;

    /** app's id */
    private Integer appId;

    /** when the record is created */
    private Date createTime;

    /** who created this record */
    private String createBy;

    /** when the record is updated */
    private Date updateTime;

    /** who updated this record */
    private String updateBy;
}