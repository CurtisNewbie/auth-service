package com.curtisnewbie.service.auth.messaging.services;

import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;

/**
 * MQ message dispatcher for auth-service
 *
 * @author yongjie.zhuang
 */
public interface AuthMessageDispatcher {

    /**
     * Dispatch AccessLog message
     */
    void dispatchAccessLog(AccessLogInfoVo vo);

    /**
     * Dispatch OperateLog message
     */
    void dispatchOperateLog(OperateLogVo vo);
}

