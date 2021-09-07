package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Application
 *
 * @author yongjie.zhuang
 */
@Data
public class AppWebVo {
    /** primary key */
    private Integer id;

    /** name of the application */
    private String name;

    /** when the record is created */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private Date createTime;

    /** who created this record */
    private String createBy;

    /** when the record is updated */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private Date updateTime;

    /** who updated this record */
    private String updateBy;
}