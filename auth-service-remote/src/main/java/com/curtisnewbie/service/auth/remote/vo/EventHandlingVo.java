package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventHandlingVo implements Serializable {

    /** primary key */
    private Integer id;

    /** type of event, 1-registration */
    private EventHandlingType type;

    /** body of the event */
    private String body;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private EventHandlingStatus status;

    /** id of user who handled the event */
    private Integer handlerId;

    /** handle result, 1-accept, 2-reject */
    private EventHandlingResult handleResult;

    /** when the event is handled */
    private LocalDateTime handleTime;
}