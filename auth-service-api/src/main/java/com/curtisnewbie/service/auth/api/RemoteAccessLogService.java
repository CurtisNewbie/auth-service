package com.curtisnewbie.service.auth.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.vo.AccessLogInfoVo;
import com.github.pagehelper.PageInfo;

/**
 * Remote service for access_log
 *
 * @author yongjie.zhuang
 */
public interface RemoteAccessLogService {

    /**
     * Find access log info in pages
     *
     * @param paging pagination param
     */
    PageInfo<AccessLogInfoVo> findAccessLogInfoByPage(PagingVo paging);
}
