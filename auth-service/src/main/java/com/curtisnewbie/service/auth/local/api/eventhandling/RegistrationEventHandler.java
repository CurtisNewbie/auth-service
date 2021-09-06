package com.curtisnewbie.service.auth.local.api.eventhandling;


import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserVo;
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

import java.util.Objects;

/**
 * <p>
 * Event handler for new user registration
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@Component
public class RegistrationEventHandler implements EventHandler {

    public static final String ROUTING_KEY = "auth.event-handler.registration";

    @Autowired
    private LocalUserService localUserService;

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = EventHandler.EVENT_HANDLER_QUEUE),
                    exchange = @Exchange(name = EventHandler.EVENT_HANDLER_EXCHANGE),
                    key = ROUTING_KEY
            ),
            ackMode = "AUTO"
    )
    @Override
    public void handle(HandleEventInfoVo info) {
        try {
            final int registeredUserid = Integer.parseInt(info.getRecord().getBody());
            final String handlerName = localUserService.findUsernameById(info.getRecord().getHandlerId());
            final EventHandlingResult result = EnumUtils.parse(info.getRecord().getHandleResult(), EventHandlingResult.class);
            if (result == null) {
                log.warn("Event handling result value illegal, unable to parse it, operation ignored, handle_result: {}",
                        info.getRecord().getHandleResult());
                return;
            }

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
                String extra = info.getExtra();
                log.info("Handled event {}, result: {}, extra: {}", info.getRecord(), result, extra);

                // only accept will actually update the user role and status
                if (!result.equals(EventHandlingResult.ACCEPT))
                    return;

                // default user role is guest
                UserRole role = UserRole.GUEST;
                if (extra != null) {
                    UserRole parsedRole = EnumUtils.parse(extra, UserRole.class);
                    if (parsedRole != null) {
                        log.info("Found extra value for user_role, using parsed user_role: {}", parsedRole);
                        role = parsedRole;
                    } else {
                        log.info("Found extra value for user_role, but value '{}' illegal, fallback to default role: {}",
                                extra, role);
                    }
                }

                // enable user
                localUserService.updateUser(UpdateUserVo.builder()
                        .id(registeredUserid)
                        .role(role)
                        .updateBy(handlerName)
                        .build());
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
