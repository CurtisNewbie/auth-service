package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.github.pagehelper.PageInfo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Remote service for access_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface RemoteAccessLogService {

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
    PageInfo<AccessLogInfoVo> findAccessLogInfoByPage(@NotNull PagingVo paging);
}
