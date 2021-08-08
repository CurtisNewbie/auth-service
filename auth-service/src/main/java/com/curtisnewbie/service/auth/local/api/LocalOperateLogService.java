package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.remote.api.RemoteOperateLogService;
import org.springframework.validation.annotation.Validated;

/**
 * Local service for operate_log
 *
 * @author yongjie.zhuang
 */
@Validated
public interface LocalOperateLogService extends RemoteOperateLogService {
}
