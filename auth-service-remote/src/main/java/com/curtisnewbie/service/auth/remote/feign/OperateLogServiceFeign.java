package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yongjie.zhuang
 */
@FeignClient(value = FeignConst.SERVICE_NAME, path = OperateLogServiceFeign.PATH)
public interface OperateLogServiceFeign {

    String PATH = "/remote/operatelog";

    /**
     * Save operate_log info
     */
    @PutMapping
    Result<Void> saveOperateLogInfo(@RequestBody OperateLogVo operateLogVo);

}
