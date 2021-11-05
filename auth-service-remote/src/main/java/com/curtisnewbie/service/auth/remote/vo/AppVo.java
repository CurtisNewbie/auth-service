package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Application
 *
 * @author yongjie.zhuang
 */
@Data
public class AppVo implements Serializable {
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