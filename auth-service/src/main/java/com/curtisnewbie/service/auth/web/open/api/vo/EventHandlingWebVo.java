package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /** body of the event */
    @JsonIgnore
    private String body;

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

    /**
     * Fill {@code description} based on type
     */
    public void fillDescription(LocalUserService localUserService) {
        EventHandlingType et = EnumUtils.parse(getType(), EventHandlingType.class);

        if (et.equals(EventHandlingType.REGISTRATION_EVENT)) {
            String username = localUserService.findUsernameById(Integer.parseInt(getBody()));
            if (username == null)
                username = "... deleted ...";
            setDescription(String.format("User '%s' requests registration approval", username));
        }
    }
}