package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime createTime;

    /** who created this record */
    private String createBy;

    /** when the record is updated */
    private LocalDateTime updateTime;

    /** who updated this record */
    private String updateBy;
}