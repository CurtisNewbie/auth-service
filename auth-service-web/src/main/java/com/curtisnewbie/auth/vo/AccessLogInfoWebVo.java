package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author yongjie.zhuang
 */
@Data
public class AccessLogInfoWebVo {

    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    /** when the user signed in */
    private Date accessTime;

    /** ip address */
    private String ipAddress;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;
}