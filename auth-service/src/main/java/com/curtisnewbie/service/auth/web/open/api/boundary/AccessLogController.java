package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.advice.RoleRequired;
import com.curtisnewbie.common.util.AssertUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.web.open.api.vo.ListAccessLogInfoReqWebVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/access")
public class AccessLogController {

    @Autowired
    private LocalAccessLogService accessLogService;

    @RoleRequired(role = "admin")
    @PostMapping("/history")
    public Result<PageableVo<List<AccessLogInfoVo>>> listAccessLogInfo(@RequestBody ListAccessLogInfoReqWebVo vo) {
        AssertUtils.nonNull(vo.getPagingVo());
        PageablePayloadSingleton<List<AccessLogInfoVo>> pps = accessLogService.findAccessLogInfoByPage(vo.getPagingVo());
        final PageableVo<List<AccessLogInfoVo>> res = new PageableVo<>();
        res.setData(pps.getPayload());
        res.setPagingVo(pps.getPagingVo());
        return Result.of(res);
    }

}
