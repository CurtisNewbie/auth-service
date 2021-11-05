package com.curtisnewbie.service.auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Application
 *
 * @author yongjie.zhuang
 */
@Data
@TableName("app")
public class App {

    /** primary key */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** name of the application */
    @TableField("name")
    private String name;

    /** when the record is created */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** who created this record */
    @TableField("create_by")
    private String createBy;

    /** when the record is updated */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /** who updated this record */
    @TableField("update_by")
    private String updateBy;
}