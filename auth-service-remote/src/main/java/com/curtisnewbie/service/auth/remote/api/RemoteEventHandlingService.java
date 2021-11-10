package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Remove service for event_handling
 *
 * @author yongjie.zhuang
 */
@Validated
public interface RemoteEventHandlingService {

    /**
     * Create an event to be handled
     *
     * @return id of the event
     */
    int createEvent(@NotNull EventHandlingVo eventHandlingVo);

    /**
     * Find events with pagination
     */
    PageablePayloadSingleton<List<EventHandlingVo>> findEventHandlingByPage(@NotNull FindEventHandlingByPageReqVo vo);

    /**
     * <p>
     * Handle the event
     * </p>
     * <p>
     * Trying to handle an event that doesn't need to be handled will have no effect
     * </p>
     */
    void handleEvent(@NotNull HandleEventReqVo vo);

}
