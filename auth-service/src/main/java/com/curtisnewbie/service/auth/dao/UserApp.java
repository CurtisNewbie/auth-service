package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.curtisnewbie.common.dao.DaoSkeleton;

import lombok.*;

/**
 * <p>
 * Join table between application and user
 * </p>
 *
 * @author yongjie.zhuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_app")
public class UserApp extends DaoSkeleton {

    /** user's id */
    @TableField("user_id")
    private Integer userId;

    /** app's id */
    @TableField("app_id")
    private Integer appId;

}