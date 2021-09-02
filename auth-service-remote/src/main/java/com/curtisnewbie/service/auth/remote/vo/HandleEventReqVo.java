package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
public class HandleEventReqVo implements Serializable {

    /** primary key */
    private int id;

    /** id of user who handled the event */
    private int handlerId;

    /** handling result */
    private EventHandlingResult result;

    /** extra */
    private String extra;

    @Builder
    public HandleEventReqVo(int id, int handlerId, EventHandlingResult result, String extra) {
        this.id = id;
        this.handlerId = handlerId;
        this.result = result;
        this.extra = extra;
    }

    public HandleEventReqVo() {
    }
}