package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * operate_log vo
 *
 * @author yongjie.zhuang
 */
@Data
public class OperateLogVo implements Serializable {

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