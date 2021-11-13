package com.curtisnewbie.service.auth.remote.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    // todo fix bug, unable to deserialize this
    /** when the operation happens */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /** parameters used for the operation */
    private String operateParam;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;

}