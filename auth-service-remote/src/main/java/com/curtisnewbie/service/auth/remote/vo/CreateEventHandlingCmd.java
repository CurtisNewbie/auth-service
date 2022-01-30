package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
@Builder
public class CreateEventHandlingCmd implements Serializable {

    /** type of event {@link EventHandlingType} */
    private EventHandlingType type;

    /** body of the event */
    private String body;

}