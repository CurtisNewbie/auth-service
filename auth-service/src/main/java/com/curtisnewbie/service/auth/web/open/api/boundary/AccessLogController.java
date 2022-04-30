package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.infrastructure.converters.AccessLogWebConverter;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.web.open.api.vo.ListAccessLogInfoReqWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.ListAccessLogInfoRespWebVo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private LocalAccessLogService accessLogService;

    @Autowired
    private AccessLogWebConverter accessLogWebConverter;

    @PostMapping("/history")
    public Result<ListAccessLogInfoRespWebVo> listAccessLogInfo(@RequestBody ListAccessLogInfoReqWebVo vo)
            throws MsgEmbeddedException {
        ValidUtils.requireNonNull(vo.getPagingVo());
        PageablePayloadSingleton<List<AccessLogInfoVo>> pps = accessLogService.findAccessLogInfoByPage(vo.getPagingVo());
        ListAccessLogInfoRespWebVo res = new ListAccessLogInfoRespWebVo();
        res.setAccessLogInfoList(mapTo(pps.getPayload(), accessLogWebConverter::toWebVo));
        res.setPagingVo(pps.getPagingVo());
        return Result.of(res);
    }

}
