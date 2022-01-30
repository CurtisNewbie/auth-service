package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.curtisnewbie.service.auth.local.vo.cmd.MoveOperateLogToHistoryCmd;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Local service for operate_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalOperateLogService {

    /**
     * Move records to operate_log_history
     */
    void moveRecordsToHistory(@NotNull MoveOperateLogToHistoryCmd cmd);

    /**
     * Save operate_log info
     */
    void saveOperateLogInfo(@NotNull OperateLogVo operateLogVo);

    /**
     * Find operate_log info in pages
     */
    PageablePayloadSingleton<List<OperateLogVo>> findOperateLogInfoInPages(@NotNull PagingVo pagingVo);
}
