package com.curtisnewbie.auth.web;

import com.curtisnewbie.auth.vo.EventHandlingWebVo;
import com.curtisnewbie.auth.vo.FindEventHandlingByPageReqWebVo;
import com.curtisnewbie.auth.vo.FindEventHandlingByPageRespWebVo;
import com.curtisnewbie.auth.vo.HandleEventReqWebVo;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.service.auth.remote.api.RemoteEventHandlingService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

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

    @PostMapping("/list")
    public Result<FindEventHandlingByPageRespWebVo> findEventHandlingByPage(@RequestBody FindEventHandlingByPageReqWebVo v)
            throws MsgEmbeddedException {
        if (v.getType() != null) {
            EventHandlingType type = EnumUtils.parse(v.getType(), EventHandlingType.class);
            ValidUtils.requireNonNull(type);
        }
        if (v.getStatus() != null) {
            EventHandlingStatus status = EnumUtils.parse(v.getStatus(), EventHandlingStatus.class);
            ValidUtils.requireNonNull(status);
        }

        PageInfo<EventHandlingVo> pi = remoteEventHandlingService.findEventHandlingByPage(
                BeanCopyUtils.toType(v, FindEventHandlingByPageReqVo.class)
        );

        FindEventHandlingByPageRespWebVo resp = new FindEventHandlingByPageRespWebVo();
        resp.setList(pi.getList().stream().map(this::loadTextDescription).collect(Collectors.toList()));
        resp.setPagingVo(new PagingVo().ofTotal(pi.getTotal()));
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

    private EventHandlingWebVo loadTextDescription(EventHandlingVo e) {
        EventHandlingWebVo wv = BeanCopyUtils.toType(e, EventHandlingWebVo.class);
        EventHandlingType et = EnumUtils.parse(e.getType(), EventHandlingType.class);

        if (et.equals(EventHandlingType.REGISTRATION_EVENT)) {
            String username = remoteUserService.findUsernameById(Integer.parseInt(e.getBody()));
            if (username == null)
                username = "   ";
            wv.setDescription(String.format("User '%s' requests registration approval", username));
        }
        return wv;
    }
}
