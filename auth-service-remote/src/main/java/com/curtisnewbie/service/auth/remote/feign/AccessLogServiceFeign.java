package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yongjie.zhuang
 */
@Validated
@FeignClient(value = FeignConst.SERVICE_NAME, path = AccessLogServiceFeign.PATH)
public interface AccessLogServiceFeign {

    String PATH = "/remote/accesslog";

    /**
     * Save access_log entity
     */
    @PutMapping
    Result<Void> save(@RequestBody AccessLogInfoVo accessLogVo);

}
