package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RequestMapping(value = AccessLogServiceFeign.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AccessLogServiceFeignController implements AccessLogServiceFeign {

    @Autowired
    private LocalAccessLogService localAccessLogService;

    @Override
    public Result<Void> save(AccessLogInfoVo accessLogVo) {
        localAccessLogService.save(accessLogVo);
        return Result.ok();
    }

}
