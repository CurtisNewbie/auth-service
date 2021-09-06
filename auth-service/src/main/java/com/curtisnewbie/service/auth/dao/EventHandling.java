package com.curtisnewbie.service.auth.dao;

import lombok.Data;

import java.util.Date;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
public class EventHandling {
    /** primary key */
    private Integer id;

    /** type of event, 1-registration */
    private Integer type;

    /** body of the event */
    private String body;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private Integer status;

    /** handle result, 1-accept, 2-reject */
    private Integer handleResult;

    /** id of user who handled the event */
    private Integer handlerId;

    /** when the event is handled */
    private Date handleTime;
}