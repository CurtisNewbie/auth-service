package com.curtisnewbie.service.auth.local.impl.eventhandling;


import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.local.api.eventhandling.AuthEventHandler;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserVo;
import com.curtisnewbie.service.auth.vo.HandleEventInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Event handler for new user registration
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@Component(AuthEventHandler.BEAN_NAME_PREFIX + "REGISTRATION_EVENT")
public class RegistrationEventHandler implements AuthEventHandler {

    @Autowired
    private LocalUserService localUserService;

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

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

            String extra = info.getExtra();
            log.info("Handled event {}, result: {}, extra: {}", info.getRecord(), result, extra);

            // only "accept" will actually update the user role and status
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
                    .isDisabled(UserIsDisabled.NORMAL)
                    .build());
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
