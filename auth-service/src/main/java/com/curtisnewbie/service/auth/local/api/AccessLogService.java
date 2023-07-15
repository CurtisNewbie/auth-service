package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Service for access_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface AccessLogService {

    /**
     * Save access_log entity
     */
    void save(@NotNull AccessLogInfoVo accessLogVo);

    /**
     * Find access log info in pages
     *
     * @param paging pagination param
     */
    @NotNull
    PageableList<AccessLogInfoVo> findAccessLogInfoByPage(@NotNull PagingVo paging);

}
