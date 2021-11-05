package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * operate log
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("operate_log")
public class OperateLog {

    /** primary key */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** name of operation */
    @TableField("operate_name")
    private String operateName;

    /** description of operation */
    @TableField("operate_desc")
    private String operateDesc;

    /** when the operation happens */
    @TableField("operate_time")
    private Date operateTime;

    /** parameters used for the operation */
    @TableField("operate_param")
    private String operateParam;

    /** username */
    @TableField("username")
    private String username;

    /** primary key of user */
    @TableField("user_id")
    private Integer userId;

}