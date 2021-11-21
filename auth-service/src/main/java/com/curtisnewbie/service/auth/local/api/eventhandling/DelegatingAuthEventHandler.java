package com.curtisnewbie.service.auth.local.api.eventhandling;


import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.vo.HandleEventInfoVo;
import com.curtisnewbie.service.auth.vo.UpdateHandleStatusReqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>
 * Abstract Event handler
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@Component
public class DelegatingAuthEventHandler {
    public static final String EVENT_HANDLER_EXCHANGE = "auth.event-handler.exg";
    public static final String EVENT_HANDLER_QUEUE = "auth.event-handler.queue";
    public static final String ROUTING_KEY = "auth.event-handler";

    @Autowired
    private LocalUserService localUserService;

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

    @Autowired
    private Map<String, AuthEventHandler> nameToAuthEventHandlers;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = EVENT_HANDLER_QUEUE),
                    exchange = @Exchange(name = EVENT_HANDLER_EXCHANGE),
                    key = ROUTING_KEY
            ),
            ackMode = "AUTO"
    )
    public void _handle(HandleEventInfoVo info) {
        // mark the event as handled, semantic lock is used here
        if (localEventHandlingService.updateHandleStatus(
                UpdateHandleStatusReqVo.builder()
                        .id(info.getRecord().getId())
                        .prevStatus(EventHandlingStatus.TO_BE_HANDLED)
                        .currStatus(EventHandlingStatus.HANDLED)
                        .handlerId(info.getRecord().getHandlerId())
                        .handleTime(info.getRecord().getHandleTime())
                        .handleResult(info.getRecord().getHandleResult())
                        .build()
        )) {
            // only handle the event when we successfully updated the status from TO_BE_HANDLED to HANDLED
            handleInternal(info);
        } else {
            log.info("Event is already handled, id: {}, skipped", info.getRecord().getId());
        }
    }

    /*** handle the event */
    public void handleInternal(HandleEventInfoVo info) {
        EventHandlingType type = EnumUtils.parse(info.getEventHandlingType(), EventHandlingType.class);
        Assert.notNull(type, "EventHandlingType == null");
        AuthEventHandler authEventHandler = nameToAuthEventHandlers.get(concatBeanName(type));
        if (authEventHandler == null) {
            // We are not able to handle this message, just drop it
            throw new AmqpRejectAndDontRequeueException("Unable to match AuthEventHandling for type: " + type);
        }
        authEventHandler.handle(info);
    }

    private static String concatBeanName(EventHandlingType type) {
        return AuthEventHandler.BEAN_NAME_PREFIX + type.name();
    }
}
