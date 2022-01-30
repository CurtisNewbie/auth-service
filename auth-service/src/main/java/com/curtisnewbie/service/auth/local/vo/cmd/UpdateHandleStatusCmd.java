package com.curtisnewbie.service.auth.local.vo.cmd;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateHandleStatusCmd {

    private int id;
    private EventHandlingStatus prevStatus;
    private EventHandlingStatus currStatus;
    private int handlerId;
    private LocalDateTime handleTime;
    private int handleResult;

    @Builder
    public UpdateHandleStatusCmd(int id, EventHandlingStatus prevStatus, EventHandlingStatus currStatus, int handlerId,
                                 LocalDateTime handleTime, int handleResult) {
        this.id = id;
        this.prevStatus = prevStatus;
        this.currStatus = currStatus;
        this.handlerId = handlerId;
        this.handleTime = handleTime;
        this.handleResult = handleResult;
    }

    public UpdateHandleStatusCmd() {
    }
}
