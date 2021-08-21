package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Access log info vo
 *
 * @author yongjie.zhuang
 */
@Data
public class AccessLogInfoVo implements Serializable {

    /** when the user signed in */
    private Date accessTime;

    /** ip address */
    private String ipAddress;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;
}