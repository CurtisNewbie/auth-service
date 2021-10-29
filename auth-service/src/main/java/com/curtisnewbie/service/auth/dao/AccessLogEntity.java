package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * access_log entity
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("access_log")
public class AccessLogEntity {

    /** primary key */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** when the user signed in */
    @TableField("access_time")
    private Date accessTime;

    /** ip address */
    @TableField("ip_address")
    private String ipAddress;

    /** username */
    @TableField("username")
    private String username;

    /** primary key of user */
    @TableField("user_id")
    private Integer userId;
}