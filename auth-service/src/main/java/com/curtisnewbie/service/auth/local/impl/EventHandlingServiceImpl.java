package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.module.messaging.service.MessagingParam;
import com.curtisnewbie.module.messaging.service.MessagingService;
import com.curtisnewbie.service.auth.dao.EventHandling;
import com.curtisnewbie.service.auth.dao.EventHandlingMapper;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.eventhandling.EventHandler;
import com.curtisnewbie.service.auth.local.api.eventhandling.RegistrationEventHandler;
import com.curtisnewbie.service.auth.remote.api.RemoteEventHandlingService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.curtisnewbie.service.auth.remote.vo.HandleEventReqVo;
import com.curtisnewbie.service.auth.vo.HandleEventInfoVo;
import com.curtisnewbie.service.auth.vo.UpdateHandleStatusReqVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
@DubboService(interfaceClass = RemoteEventHandlingService.class)
public class EventHandlingServiceImpl implements LocalEventHandlingService {

    @Autowired
    private EventHandlingMapper mapper;

    @Autowired
    private MessagingService messagingService;

    @Override
    public int createEvent(@NotNull EventHandlingVo vo) {
        final EventHandling eventHandling = BeanCopyUtils.toType(vo, EventHandling.class);
        validateStatus(eventHandling.getStatus());
        validateType(eventHandling.getType());

        mapper.insert(eventHandling);
        return eventHandling.getId();
    }

    @Override
    public PageInfo<EventHandlingVo> findEventHandlingByPage(@NotNull FindEventHandlingByPageReqVo vo) {
        final EventHandling eventHandling = BeanCopyUtils.toType(vo, EventHandling.class);
        if (eventHandling.getStatus() != null)
            validateStatus(eventHandling.getStatus());
        if (eventHandling.getType() != null)
            validateType(eventHandling.getType());
        Objects.requireNonNull(vo.getPagingVo(), "pagingVo == null");

        PageHelper.startPage(vo.getPagingVo().getPage(), vo.getPagingVo().getLimit());
        PageInfo<EventHandling> pi = PageInfo.of(mapper.selectByPage(eventHandling));
        return BeanCopyUtils.toPageList(pi, EventHandlingVo.class);
    }

    @Override
    public void handleEvent(@NotNull HandleEventReqVo vo) {
        EventHandling eh = mapper.selectByPrimaryKey(vo.getId());
        eh.setHandlerId(vo.getHandlerId());
        eh.setHandleTime(new Date());

        EventHandlingType type = EnumUtils.parse(eh.getType(), EventHandlingType.class);
        String routingKey;
        switch (type) {
            case REGISTRATION_EVENT:
                routingKey = RegistrationEventHandler.ROUTING_KEY;
                break;
            default:
                throw new IllegalArgumentException("Does not support " + type.toString());
        }

        // send to workers
        messagingService.send(MessagingParam.builder()
                .payload(HandleEventInfoVo.builder()
                        .record(eh)
                        .extra(vo.getExtra())
                        .result(vo.getResult())
                        .build())
                .exchange(EventHandler.EVENT_HANDLER_EXCHANGE)
                .routingKey(routingKey)
                .deliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build());
    }

    private void validateType(Integer typeValue) {
        final EventHandlingType type = EnumUtils.parse(typeValue, EventHandlingType.class);
        if (type == null) {
            log.error("Type value Illegal, cannot be parsed, value: {}", typeValue);
            throw new IllegalArgumentException("type value illegal");
        }
    }

    private void validateStatus(Integer statusValue) {
        final EventHandlingStatus status = EnumUtils.parse(statusValue, EventHandlingStatus.class);
        if (status == null) {
            log.error("Status value Illegal, cannot be parsed, value: {}", statusValue);
            throw new IllegalArgumentException("status value illegal");
        }
    }

    @Override
    public boolean updateHandleStatus(@NotNull UpdateHandleStatusReqVo vo) {
        return mapper.updateHandlingResult(vo.getId(),
                vo.getPrevStatus().getValue(),
                vo.getCurrStatus().getValue(),
                vo.getHandlerId(),
                vo.getHandleTime()) == 1;
    }
}
