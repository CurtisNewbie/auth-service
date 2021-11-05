package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Application basic info
 *
 * @author yongjie.zhuang
 */
@Data
public class AppBriefVo implements Serializable {
    /** primary key */
    private Integer id;

    /** name of the application */
    private String name;
}