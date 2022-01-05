package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RestController
public class OperateLogServiceController implements OperateLogServiceFeign {

    @Autowired
    private LocalOperateLogService localOperateLogService;

    @Override
    public Result<Void> saveOperateLogInfo(OperateLogVo operateLogVo) {
        localOperateLogService.saveOperateLogInfo(operateLogVo);
        return Result.ok();
    }

    @Override
    public Result<PageablePayloadSingleton<List<OperateLogVo>>> findOperateLogInfoInPages(PagingVo pagingVo) {
        return Result.of(localOperateLogService.findOperateLogInfoInPages(pagingVo));
    }
}
