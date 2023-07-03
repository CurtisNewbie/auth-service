package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Access log info vo
 *
 * @author yongjie.zhuang
 */
@Data
public class AccessLogInfoVo implements Serializable {

    private Integer id;

    /** when the user signed in */
    private LocalDateTime accessTime;

    /** ip address */
    private String ipAddress;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;

    /** request url */
    private String url;

    /** user agent */
    private String userAgent;
}