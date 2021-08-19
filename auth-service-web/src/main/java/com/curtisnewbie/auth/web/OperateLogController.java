package com.curtisnewbie.auth.web;

import com.curtisnewbie.auth.vo.FindOperateLogRespVo;
import com.curtisnewbie.auth.vo.OperateLogWebVo;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.service.auth.remote.api.RemoteOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/operate")
public class OperateLogController {

    @DubboReference
    private RemoteOperateLogService remoteOperateLogService;

    @LogOperation(name = "/operate/history", description = "find operate log history in pages", enabled = false)
    @PostMapping("/history")
    public Result<FindOperateLogRespVo> findByPage(@RequestBody PagingVo pagingVo) throws MsgEmbeddedException {
        ValidUtils.requireNonNull(pagingVo.getLimit());
        ValidUtils.requireNonNull(pagingVo.getTotal());

        PageInfo<OperateLogVo> pv = remoteOperateLogService.findOperateLogInfoInPages(pagingVo);
        FindOperateLogRespVo res = new FindOperateLogRespVo();
        res.setPagingVo(new PagingVo().ofTotal(pv.getTotal()));
        res.setOperateLogVoList(BeanCopyUtils.toTypeList(pv.getList(), OperateLogWebVo.class));
        return Result.of(res);
    }

}