package com.curtisnewbie.service.auth.dao;

import lombok.Data;

import java.util.Date;

/**
 * access_log entity
 *
 * @author yongjie.zhuang
 */
@Data
public class AccessLogEntity {

    /** primary key */
    private Integer id;

    /** when the user signed in */
    private Date accessTime;

    /** ip address */
    private String ipAddress;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;
}