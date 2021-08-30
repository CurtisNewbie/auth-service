package com.curtisnewbie.service.auth.vo;

import com.curtisnewbie.service.auth.dao.EventHandling;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
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
     * result
     */
    private EventHandlingResult result;

    @Builder
    public HandleEventInfoVo(EventHandling record, EventHandlingResult result) {
        this.record = record;
        this.result = result;
    }

}
