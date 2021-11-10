package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Remote service for operate_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface RemoteOperateLogService {

    /**
     * Save operate_log info
     */
    void saveOperateLogInfo(@NotNull OperateLogVo operateLogVo);

    /**
     * Find operate_log info in pages
     */
    PageablePayloadSingleton<List<OperateLogVo>> findOperateLogInfoInPages(@NotNull PagingVo pagingVo);

}
