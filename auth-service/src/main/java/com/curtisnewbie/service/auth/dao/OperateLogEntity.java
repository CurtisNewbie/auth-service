package com.curtisnewbie.service.auth.dao;

import lombok.Data;

import java.util.Date;

/**
 * operate log
 *
 * @author yongjie.zhuang
 */
@Data
public class OperateLogEntity {
    /** primary key */
    private Integer id;

    /** name of operation */
    private String operateName;

    /** description of operation */
    private String operateDesc;

    /** when the operation happens */
    private Date operateTime;

    /** parameters used for the operation */
    private String operateParam;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;

}