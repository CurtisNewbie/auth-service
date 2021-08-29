package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.api.RemoteOperateLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Local service for operate_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalOperateLogService extends RemoteOperateLogService {

    /**
     * Find ids of records where the operate_log is before the given date
     */
    @NotNull
    PageInfo<Integer> findIdsBeforeDateByPage(@NotNull PagingVo paging, @NotNull Date date);

    /**
     * Move the records to operate_log_history
     * <p>
     * Records are moved to operate_log_history
     * </p>
     */
    void moveRecordsToHistory(@NotNull List<Integer> ids);
}
