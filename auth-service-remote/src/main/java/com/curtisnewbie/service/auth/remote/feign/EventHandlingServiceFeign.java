package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.CreateEventHandlingCmd;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RequestMapping("/remote/event")
@FeignClient(FeignConst.SERVICE_NAME)
public interface EventHandlingServiceFeign {

    /**
     * Create an event to be handled
     *
     * @return id of the event
     */
    @PutMapping
    Result<Integer> createEvent(@RequestBody CreateEventHandlingCmd cmd);

    /**
     * Find events with pagination
     */
    @PostMapping("/list")
    Result<PageablePayloadSingleton<List<EventHandlingVo>>> findEventHandlingByPage(@RequestBody FindEventHandlingByPageReqVo vo);

    /**
     * <p>
     * Handle the event
     * </p>
     * <p>
     * Trying to handle an event that doesn't need to be handled will have no effect
     * </p>
     */
    @PostMapping("/handling")
    Result<Void> handleEvent(@NotNull HandleEventReqVo vo);
}

