package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.CreateEventHandlingCmd;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yongjie.zhuang
 */
@FeignClient(value = FeignConst.SERVICE_NAME, path = EventHandlingServiceFeign.PATH)
public interface EventHandlingServiceFeign {

    String PATH = "/remote/event";

    /**
     * Create an event to be handled
     *
     * @return id of the event
     */
    @PutMapping
    Result<Integer> createEvent(@RequestBody CreateEventHandlingCmd cmd);

}

