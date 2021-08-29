package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.github.pagehelper.PageInfo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Remove service for event_handling
 *
 * @author yongjie.zhuang
 */
@Validated
public interface RemoveEventHandlingService {

    /**
     * Create an event to be handled
     *
     * @return id of the event
     */
    int createEvent(@NotNull EventHandlingVo eventHandlingVo);

    /**
     * Find events with pagination
     */
    PageInfo<EventHandlingVo> findEventHandlingByPage(@NotNull FindEventHandlingByPageReqVo vo);

    /**
     * Update event status
     *
     * @param id         id
     * @param prevStatus previous status
     * @param currStatus current status
     */
    void updateStatus(int id, int prevStatus, int currStatus);

}
