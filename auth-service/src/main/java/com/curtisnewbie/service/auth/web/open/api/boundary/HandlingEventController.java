package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.trace.TraceUtils;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import com.curtisnewbie.service.auth.web.open.api.vo.EventHandlingWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.FindEventHandlingByPageReqWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.FindEventHandlingByPageRespWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.HandleEventReqWebVo;
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
@RequestMapping("${web.base-path}/event")
public class HandlingEventController {

    @Autowired
    private LocalEventHandlingService eventHandlingService;

    @Autowired
    private LocalUserService userService;

    @PostMapping("/list")
    public Result<FindEventHandlingByPageRespWebVo> findEventHandlingByPage(@RequestBody FindEventHandlingByPageReqWebVo v) {

        final PageablePayloadSingleton<List<EventHandlingVo>> pi = eventHandlingService.findEventHandlingByPage(
                BeanCopyUtils.toType(v, FindEventHandlingByPageReqVo.class)
        );

        final FindEventHandlingByPageRespWebVo resp = new FindEventHandlingByPageRespWebVo();
        resp.setList(mapTo(pi.getPayload(), e -> BeanCopyUtils.toType(e, EventHandlingWebVo.class)));
        resp.setPagingVo(pi.getPagingVo());
        return Result.of(resp);
    }

    @PostMapping("/handle")
    public Result<Void> handleEvent(@RequestBody HandleEventReqWebVo v) throws MsgEmbeddedException {
        ValidUtils.requireNonNull(v.getId());
        ValidUtils.requireNonNull(v.getResult());
        ValidUtils.requireNonNull(v.getResult());
        eventHandlingService.handleEvent(HandleEventReqVo.builder()
                .id(v.getId())
                .result(v.getResult())
                .extra(v.getExtra())
                .handlerId(TraceUtils.tUser().getUserId())
                .build());
        return Result.ok();
    }
}
