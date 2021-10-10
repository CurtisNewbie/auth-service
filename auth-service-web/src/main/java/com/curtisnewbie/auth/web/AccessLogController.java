package com.curtisnewbie.auth.web;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.curtisnewbie.auth.config.SentinelFallbackConfig;
import com.curtisnewbie.auth.vo.AccessLogInfoWebVo;
import com.curtisnewbie.auth.vo.ListAccessLogInfoReqVo;
import com.curtisnewbie.auth.vo.ListAccessLogInfoRespVo;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/access")
public class AccessLogController {

    @DubboReference(lazy = true)
    private RemoteAccessLogService accessLogService;

    @SentinelResource(value = "listAccessLogInfo", defaultFallback = "serviceNotAvailable",
            fallbackClass = SentinelFallbackConfig.class)
    @LogOperation(name = "/access/history", description = "list access log info")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/history")
    public Result<ListAccessLogInfoRespVo> listAccessLogInfo(@RequestBody ListAccessLogInfoReqVo vo)
            throws MsgEmbeddedException {
        ValidUtils.requireNonNull(vo.getPagingVo());
        PageInfo<AccessLogInfoVo> pageInfo = accessLogService.findAccessLogInfoByPage(vo.getPagingVo());
        PagingVo paging = new PagingVo();
        paging.setTotal(pageInfo.getTotal());
        ListAccessLogInfoRespVo res = new ListAccessLogInfoRespVo(BeanCopyUtils.toTypeList(pageInfo.getList(),
                AccessLogInfoWebVo.class));
        res.setPagingVo(paging);
        return Result.of(res);
    }

}
