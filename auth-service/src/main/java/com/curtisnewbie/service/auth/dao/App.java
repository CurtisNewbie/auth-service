package com.curtisnewbie.service.auth.dao;

import lombok.Data;

import java.util.Date;

/**
 * Application
 *
 * @author yongjie.zhuang
 */
@Data
public class App {
    /** primary key */
    private Integer id;

    /** name of the application */
    private String name;

    /** when the record is created */
    private Date createTime;

    /** who created this record */
    private String createBy;

    /** when the record is updated */
    private Date updateTime;

    /** who updated this record */
    private String updateBy;
}