package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.curtisnewbie.common.dao.DaoSkeleton;
import lombok.Data;

/**
 * User entity
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("user")
public class User extends DaoSkeleton {

    /** primary key */
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

    /** whether the user is disabled, 0-normal, 1-disabled */
    @TableField("is_disabled")
    private Integer isDisabled;
}