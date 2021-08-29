package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Service for access_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalAccessLogService extends RemoteAccessLogService {

    /**
     * Find ids of records where the access_date is before the given date
     */
    @NotNull
    PageInfo<Integer> findIdsBeforeDateByPage(@NotNull PagingVo paging, @NotNull Date date);

    /**
     * Move the records to access_log_history
     * <p>
     * Records are moved to access_log_history
     * </p>
     */
    void moveRecordsToHistory(@NotNull List<Integer> ids);

}
