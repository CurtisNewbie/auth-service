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
     * extra
     */
    private String extra;

    @Builder
    public HandleEventInfoVo(EventHandling record, String extra) {
        this.record = record;
        this.extra = extra;
    }

}
