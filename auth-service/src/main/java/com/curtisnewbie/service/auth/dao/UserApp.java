package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("user_app")
public class UserApp {

    /** user's id */
    @TableField("user_id")
    private Integer userId;

    /** app's id */
    @TableField("app_id")
    private Integer appId;

    /** when the record is created */
    @TableField("create_time")
    private Date createTime;

    /** who created this record */
    @TableField("create_by")
    private String createBy;

    /** when the record is updated */
    @TableField("update_time")
    private Date updateTime;

    /** who updated this record */
    private String updateBy;
}