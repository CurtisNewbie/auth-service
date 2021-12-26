package com.curtisnewbie.auth.web;

import com.curtisnewbie.auth.converters.OperateLogWebConverter;
import com.curtisnewbie.auth.vo.FindOperateLogRespVo;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.service.auth.remote.api.RemoteOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/operate")
public class OperateLogController {

    @DubboReference
    private RemoteOperateLogService remoteOperateLogService;

    @Autowired
    private OperateLogWebConverter cvtr;

    @PreAuthorize("hasAuthority('admin')")
    @LogOperation(name = "/operate/history", description = "find operate log history in pages", enabled = false)
    @PostMapping("/history")
    public Result<FindOperateLogRespVo> findByPage(@RequestBody PagingVo pagingVo) throws MsgEmbeddedException {
        PageablePayloadSingleton<List<OperateLogVo>> pv = remoteOperateLogService.findOperateLogInfoInPages(pagingVo);
        FindOperateLogRespVo res = new FindOperateLogRespVo();
        res.setPagingVo(pv.getPagingVo());
        res.setOperateLogVoList(BeanCopyUtils.mapTo(pv.getPayload(), cvtr::toWebVo));
        return Result.of(res);
    }

}
