package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
public class EventHandlingWebVo {

    /** primary key */
    private Integer id;

    /** type of event, 1-registration */
    private EventHandlingType type;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private EventHandlingStatus status;

    /** id of user who handled the event */
    private Integer handlerId;

    /** handle result, 1-accept, 2-reject */
    private EventHandlingResult handleResult;

    /** when the event is handled */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private LocalDateTime handleTime;

    /** body of the event */
    @JsonIgnore
    private String body;

    /**
     * A description of the event
     */
    private String description;
}