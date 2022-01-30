package com.curtisnewbie.service.auth.local.impl.eventhandling;


import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.local.api.eventhandling.AuthEventHandler;
import com.curtisnewbie.service.auth.local.vo.cmd.HandleEventInfoVo;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.vo.UserRequestAppApprovalCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.curtisnewbie.common.util.JsonUtils.readValueAsObject;

/**
 * <p>
 * Handler of events of user requesting approval for using the app
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@Component(AuthEventHandler.BEAN_NAME_PREFIX + "REQUEST_APP_APPROVAL")
public class RequestAppApprovalEventHandler implements AuthEventHandler {

    @Autowired
    private LocalUserAppService localUserAppService;
    @Autowired
    private LocalUserService localUserService;

    @Override
    public void handle(HandleEventInfoVo info) {
        try {
            final UserRequestAppApprovalCmd cmd = readValueAsObject(info.getRecord().getBody(), UserRequestAppApprovalCmd.class);
            if (cmd == null) {
                log.error("Failed to parse {}, body: {}", UserRequestAppApprovalCmd.class.getSimpleName(), info.getRecord().getBody());
                return;
            }
            if (cmd.isInvalid()) {
                log.error("Command object invalid: {}", cmd);
                return;
            }

            final String handlerName = localUserService.findUsernameById(info.getRecord().getHandlerId());
            final EventHandlingResult result = info.getRecord().getHandleResult();
            if (result == null) {
                log.warn("Event handling result value illegal, unable to parse it, operation ignored, handle_result: {}",
                        info.getRecord().getHandleResult());
                return;
            }

            if (result != EventHandlingResult.ACCEPT)
                localUserAppService.addUserApp(cmd.getUserId(), cmd.getAppId(), handlerName);

        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
