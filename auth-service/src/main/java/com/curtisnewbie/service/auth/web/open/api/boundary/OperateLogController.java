package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.infrastructure.converters.OperateLogWebConverter;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.curtisnewbie.service.auth.web.open.api.vo.FindOperateLogRespWebVo;
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

    @Autowired
    private LocalOperateLogService operateLogService;

    @Autowired
    private OperateLogWebConverter cvtr;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/history")
    public Result<FindOperateLogRespWebVo> findByPage(@RequestBody PagingVo pagingVo) throws MsgEmbeddedException {
        PageablePayloadSingleton<List<OperateLogVo>> pv = operateLogService.findOperateLogInfoInPages(pagingVo);
        FindOperateLogRespWebVo res = new FindOperateLogRespWebVo();
        res.setPagingVo(pv.getPagingVo());
        res.setOperateLogVoList(BeanCopyUtils.mapTo(pv.getPayload(), cvtr::toWebVo));
        return Result.of(res);
    }

}
