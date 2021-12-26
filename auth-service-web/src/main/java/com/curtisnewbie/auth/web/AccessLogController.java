package com.curtisnewbie.auth.web;

import com.curtisnewbie.auth.converters.AccessLogWebConverter;
import com.curtisnewbie.auth.vo.ListAccessLogInfoReqVo;
import com.curtisnewbie.auth.vo.ListAccessLogInfoRespVo;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.curtisnewbie.common.util.BeanCopyUtils.mapTo;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/access")
public class AccessLogController {

    @DubboReference
    private RemoteAccessLogService accessLogService;

    @Autowired
    private AccessLogWebConverter accessLogWebConverter;

    @LogOperation(name = "/access/history", description = "list access log info")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/history")
    public Result<ListAccessLogInfoRespVo> listAccessLogInfo(@RequestBody ListAccessLogInfoReqVo vo)
            throws MsgEmbeddedException {
        ValidUtils.requireNonNull(vo.getPagingVo());
        PageablePayloadSingleton<List<AccessLogInfoVo>> pps = accessLogService.findAccessLogInfoByPage(vo.getPagingVo());
        ListAccessLogInfoRespVo res = new ListAccessLogInfoRespVo();
        res.setAccessLogInfoList(mapTo(pps.getPayload(), accessLogWebConverter::toWebVo));
        res.setPagingVo(pps.getPagingVo());
        return Result.of(res);
    }

}
