package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogVo;
import com.github.pagehelper.PageInfo;

/**
 * Remote service for access_log
 *
 * @author yongjie.zhuang
 */
public interface RemoteAccessLogService {

    /**
     * Save access_log entity
     */
    void save(AccessLogVo accessLogVo);

    /**
     * Find access log info in pages
     *
     * @param paging pagination param
     */
    PageInfo<AccessLogInfoVo> findAccessLogInfoByPage(PagingVo paging);
}
