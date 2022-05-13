package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.service.auth.remote.vo.CreateEventHandlingCmd;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import com.curtisnewbie.service.auth.local.vo.cmd.UpdateHandleStatusCmd;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Local service for event_handling
 *
 * @author yongjie.zhuang
 */
@Deprecated // todo delete this
public interface LocalEventHandlingService {

    /**
     * Update handling result
     *
     * @return record updated
     */
    boolean updateHandleStatus(@NotNull UpdateHandleStatusCmd param);

    /**
     * Find by id
     *
     * @param id id
     */
    EventHandlingVo findById(int id);

    /**
     * Create an event to be handled
     *
     * @return id of the event
     */
    int createEvent(@Validated @NotNull CreateEventHandlingCmd cmd);

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
