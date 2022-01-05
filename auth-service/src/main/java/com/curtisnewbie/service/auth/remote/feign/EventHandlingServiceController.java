package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.remote.vo.CreateEventHandlingCmd;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RestController
public class EventHandlingServiceController implements EventHandlingServiceFeign {

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

    @Override
    public Result<Integer> createEvent(CreateEventHandlingCmd cmd) {
        return Result.of(localEventHandlingService.createEvent(cmd));
    }

    @Override
    public Result<PageablePayloadSingleton<List<EventHandlingVo>>> findEventHandlingByPage(FindEventHandlingByPageReqVo vo) {
        return Result.of(localEventHandlingService.findEventHandlingByPage(vo));
    }

    @Override
    public Result<Void> handleEvent(@NotNull HandleEventReqVo vo) {
        localEventHandlingService.handleEvent(vo);
        return Result.ok();
    }
}
