package com.curtisnewbie.service.auth.messaging.routing;

/**
 * Constants for Mq message routing
 *
 * @author yongjie.zhuang
 */
public enum RoutingEnum {

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

    RoutingEnum(String routingKey, String exchange) {
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
