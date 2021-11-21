package com.curtisnewbie.service.auth.vo;

import com.curtisnewbie.service.auth.dao.EventHandling;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Info of event's handling
 *
 * @author yongjie.zhuang
 */
@Data
@NoArgsConstructor
public class HandleEventInfoVo {

    /**
     * record
     */
    private EventHandling record;

    /**
     * extra
     */
    private String extra;

    /** type of the event */
    private Integer eventHandlingType;

    @Builder
    public HandleEventInfoVo(EventHandling record, String extra, EventHandlingType type) {
        this.record = record;
        this.extra = extra;
        this.eventHandlingType = type.getValue();
    }
}
