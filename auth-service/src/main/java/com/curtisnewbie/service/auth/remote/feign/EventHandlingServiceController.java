package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.remote.vo.CreateEventHandlingCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RequestMapping(value = EventHandlingServiceFeign.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class EventHandlingServiceController implements EventHandlingServiceFeign {

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

    @Override
    public Result<Integer> createEvent(CreateEventHandlingCmd cmd) {
        return Result.of(localEventHandlingService.createEvent(cmd));
    }

}
