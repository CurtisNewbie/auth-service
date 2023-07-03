package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * operate_log web vo
 *
 * @author yongjie.zhuang
 */
@Data
public class OperateLogWebVo {

    /** name of operation */
    private String operateName;

    /** description of operation */
    private String operateDesc;

    /** when the operation happens */
    private LocalDateTime operateTime;

    /** parameters used for the operation */
    private String operateParam;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;

}