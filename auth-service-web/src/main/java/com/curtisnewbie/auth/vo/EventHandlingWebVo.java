package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
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
    private Integer type;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private Integer status;

    /** id of user who handled the event */
    private Integer handlerId;

    /** handle result, 1-accept, 2-reject */
    private Integer handleResult;

    /** when the event is handled */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private LocalDateTime handleTime;

    /**
     * A description of the event
     */
    private String description;

    @Builder
    public EventHandlingWebVo(Integer id, Integer type, Integer status, Integer handlerId, Integer handleResult,
                              LocalDateTime handleTime, String description) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.handlerId = handlerId;
        this.handleResult = handleResult;
        this.handleTime = handleTime;
        this.description = description;
    }

    public EventHandlingWebVo() {
    }
}