package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.curtisnewbie.common.dao.DaoSkeleton;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.consts.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User entity
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("user")
@EqualsAndHashCode
public class User extends DaoSkeleton {

    /** username (must be unique) */
    @TableField("username")
    private String username;

    /** password in hash */
    @TableField("password")
    private String password;

    /** salt */
    @TableField("salt")
    private String salt;

    /** user type */
    @TableField("type")
    private UserType type;

    /** role */
    @TableField("role")
    private UserRole role;

    /** whether the user is disabled, 0-normal, 1-disabled */
    @TableField("is_disabled")
    private UserIsDisabled isDisabled;
}