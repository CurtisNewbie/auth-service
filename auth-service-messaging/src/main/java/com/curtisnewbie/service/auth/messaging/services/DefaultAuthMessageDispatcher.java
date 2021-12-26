package com.curtisnewbie.service.auth.messaging.services;

import com.curtisnewbie.module.messaging.service.MessagingParam;
import com.curtisnewbie.module.messaging.service.MessagingService;
import com.curtisnewbie.service.auth.messaging.routing.AuthServiceRoutingInfo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yongjie.zhuang
 */
@Component
public class DefaultAuthMessageDispatcher implements AuthMessageDispatcher {

    @Autowired
    private MessagingService messagingService;

    @Override
    public void dispatchAccessLog(AccessLogInfoVo vo) {
        messagingService.send(MessagingParam.builder()
                .payload(vo)
                .exchange(AuthServiceRoutingInfo.SAVE_ACCESS_LOG_ROUTING.getExchange())
                .routingKey(AuthServiceRoutingInfo.SAVE_ACCESS_LOG_ROUTING.getRoutingKey())
                .deliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build());
    }

    @Override
    public void dispatchOperateLog(OperateLogVo vo) {
        messagingService.send(MessagingParam.builder()
                .payload(vo)
                .exchange(AuthServiceRoutingInfo.SAVE_OPERATE_LOG_ROUTING.getExchange())
                .routingKey(AuthServiceRoutingInfo.SAVE_OPERATE_LOG_ROUTING.getRoutingKey())
                .deliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build());
    }
}
