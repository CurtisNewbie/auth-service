package com.curtisnewbie.service.auth.web;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.service.auth.remote.api.RemoteEventHandlingService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import com.curtisnewbie.service.auth.vo.EventHandlingWebVo;
import com.curtisnewbie.service.auth.vo.FindEventHandlingByPageReqWebVo;
import com.curtisnewbie.service.auth.vo.FindEventHandlingByPageRespWebVo;
import com.curtisnewbie.service.auth.vo.HandleEventReqWebVo;
import org.apache.dubbo.config.annotation.DubboReference;
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
@RequestMapping("${web.base-path}/event")
public class HandlingEventController {

    @DubboReference
    private RemoteEventHandlingService remoteEventHandlingService;

    @DubboReference
    private RemoteUserService remoteUserService;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/list")
    public Result<FindEventHandlingByPageRespWebVo> findEventHandlingByPage(@RequestBody FindEventHandlingByPageReqWebVo v)
            throws MsgEmbeddedException {

        v.validate();

        PageablePayloadSingleton<List<EventHandlingVo>> pi = remoteEventHandlingService.findEventHandlingByPage(
                BeanCopyUtils.toType(v, FindEventHandlingByPageReqVo.class)
        );

        FindEventHandlingByPageRespWebVo resp = new FindEventHandlingByPageRespWebVo();
        resp.setList(mapTo(pi.getPayload(), this::fillTextDescription));
        resp.setPagingVo(pi.getPagingVo());
        return Result.of(resp);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/handle")
    public Result<Void> handleEvent(@RequestBody HandleEventReqWebVo v) throws InvalidAuthenticationException,
            MsgEmbeddedException {
        ValidUtils.requireNonNull(v.getId());
        ValidUtils.requireNonNull(v.getResult());
        EventHandlingResult result = EnumUtils.parse(v.getResult(), EventHandlingResult.class);
        ValidUtils.requireNonNull(result);
        remoteEventHandlingService.handleEvent(HandleEventReqVo.builder()
                .id(v.getId())
                .result(result)
                .extra(v.getExtra())
                .handlerId(AuthUtil.getUserId())
                .build());
        return Result.ok();
    }

    private EventHandlingWebVo fillTextDescription(EventHandlingVo e) {
        EventHandlingWebVo wv = BeanCopyUtils.toType(e, EventHandlingWebVo.class);
        wv.fillDescription(remoteUserService);
        return wv;
    }
}
