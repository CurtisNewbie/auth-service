package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.advice.RoleControlled;
import com.curtisnewbie.common.util.AssertUtils;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.goauth.client.PathDoc;
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

    @PathDoc(description = "List access logs")
    @RoleControlled(rolesRequired = "admin")
    @PostMapping("/history")
    public Result<PageableVo<List<AccessLogInfoVo>>> listAccessLogInfo(@RequestBody ListAccessLogInfoReqWebVo vo) {
        AssertUtils.nonNull(vo.getPagingVo());
        PageableList<AccessLogInfoVo> pps = accessLogService.findAccessLogInfoByPage(vo.getPagingVo());
        final PageableVo<List<AccessLogInfoVo>> res = new PageableVo<>();
        res.setPayload(pps.getPayload());
        res.setPagingVo(pps.getPagingVo());
        return Result.of(res);
    }

}
