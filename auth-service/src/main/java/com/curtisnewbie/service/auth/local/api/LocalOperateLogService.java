package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.remote.api.RemoteOperateLogService;
import com.curtisnewbie.service.auth.vo.MoveOperateLogToHistoryCmd;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Local service for operate_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalOperateLogService extends RemoteOperateLogService {

    /**
     * Move records to operate_log_history
     */
    void moveRecordsToHistory(@NotNull MoveOperateLogToHistoryCmd cmd);
}
