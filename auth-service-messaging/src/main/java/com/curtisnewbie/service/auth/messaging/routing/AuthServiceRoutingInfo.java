package com.curtisnewbie.service.auth.messaging.routing;

import com.curtisnewbie.module.messaging.config.RoutingInfo;

/**
 * Constants for auth-service Mq message routing
 *
 * @author yongjie.zhuang
 */
public enum AuthServiceRoutingInfo implements RoutingInfo {

    /**
     * save access_log
     */
    SAVE_ACCESS_LOG_ROUTING("auth.access-log.save", "auth.access-log.exg"),

    /**
     * save operate_log
     */
    SAVE_OPERATE_LOG_ROUTING("auth.operate-log.save", "auth.operate-log.exg");

    private final String routingKey;
    private final String exchange;

    AuthServiceRoutingInfo(String routingKey, String exchange) {
        this.routingKey = routingKey;
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getExchange() {
        return exchange;
    }
}
