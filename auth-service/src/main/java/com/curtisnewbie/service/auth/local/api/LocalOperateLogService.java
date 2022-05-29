package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.local.vo.cmd.MoveOperateLogToHistoryCmd;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

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
    PageableList<OperateLogVo> findOperateLogInfoInPages(@NotNull PagingVo pagingVo);
}
