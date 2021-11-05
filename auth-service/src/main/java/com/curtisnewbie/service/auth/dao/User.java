package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * User entity
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("user")
public class User {

    /** primary key */
    @TableField("id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** username (must be unique) */
    @TableField("username")
    private String username;

    /** password in hash */
    @TableField("password")
    private String password;

    /** salt */
    @TableField("salt")
    private String salt;

    /** role */
    @TableField("role")
    private String role;

    /** when the user is created */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** when the user is updated */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /** whether the user is disabled, 0-normal, 1-disabled */
    @TableField("is_disabled")
    private Integer isDisabled;

    /** who updated this user */
    @TableField("update_by")
    private String updateBy;

    /** who created this user */
    @TableField("create_by")
    private String createBy;
}