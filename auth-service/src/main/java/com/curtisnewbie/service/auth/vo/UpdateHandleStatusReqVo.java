package com.curtisnewbie.service.auth.vo;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateHandleStatusReqVo {

    private int id;
    private EventHandlingStatus prevStatus;
    private EventHandlingStatus currStatus;
    private int handlerId;
    private LocalDateTime handleTime;
    private int handleResult;

    @Builder
    public UpdateHandleStatusReqVo(int id, EventHandlingStatus prevStatus, EventHandlingStatus currStatus, int handlerId,
                                   LocalDateTime handleTime, int handleResult) {
        this.id = id;
        this.prevStatus = prevStatus;
        this.currStatus = currStatus;
        this.handlerId = handlerId;
        this.handleTime = handleTime;
        this.handleResult = handleResult;
    }

    public UpdateHandleStatusReqVo() {
    }
}
