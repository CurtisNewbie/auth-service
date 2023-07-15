package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.OperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RequestMapping(value = OperateLogServiceFeign.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class OperateLogServiceController implements OperateLogServiceFeign {

    @Autowired
    private OperateLogService localOperateLogService;

    @Override
    public Result<Void> saveOperateLogInfo(OperateLogVo operateLogVo) {
        localOperateLogService.saveOperateLogInfo(operateLogVo);
        return Result.ok();
    }
}
