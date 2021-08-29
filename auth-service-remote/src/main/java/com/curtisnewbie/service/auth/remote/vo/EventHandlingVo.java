package com.curtisnewbie.service.auth.remote.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
public class EventHandlingVo {

    /** primary key */
    private Integer id;

    /** type of event, 1-registration */
    private Integer type;

    /** body of the event */
    private String body;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private Integer status;

    /** id of user who handled the event */
    private Integer handlerId;

    /** when the event is handled */
    private Date handleTime;

    @Builder
    public EventHandlingVo(Integer id, Integer type, String body, Integer status, Integer handlerId, Date handleTime) {
        this.id = id;
        this.type = type;
        this.body = body;
        this.status = status;
        this.handlerId = handlerId;
        this.handleTime = handleTime;
    }

    public EventHandlingVo() {
    }
}