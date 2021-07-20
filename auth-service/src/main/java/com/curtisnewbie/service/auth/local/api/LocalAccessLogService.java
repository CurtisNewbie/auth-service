package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.dao.AccessLogEntity;
import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;

/**
 * Service for access_log
 *
 * @author yongjie.zhuang
 */
public interface LocalAccessLogService extends RemoteAccessLogService {

    /**
     * Save access_log entity
     *
     * @param accessLogEntity
     */
    void save(AccessLogEntity accessLogEntity);

}
