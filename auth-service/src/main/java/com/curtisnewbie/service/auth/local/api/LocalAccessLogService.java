package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.curtisnewbie.service.auth.vo.MoveAccessLogToHistoryCmd;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Service for access_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalAccessLogService extends RemoteAccessLogService {

    /**
     * Move records to access_log_history
     */
    void moveRecordsToHistory(@NotNull MoveAccessLogToHistoryCmd cmd);

}
