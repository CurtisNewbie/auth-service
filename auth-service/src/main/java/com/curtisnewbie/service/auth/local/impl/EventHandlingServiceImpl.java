package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.service.auth.dao.EventHandling;
import com.curtisnewbie.service.auth.dao.EventHandlingMapper;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.remote.api.RemoveEventHandlingService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
@DubboService(interfaceClass = RemoveEventHandlingService.class)
public class EventHandlingServiceImpl implements LocalEventHandlingService {

    @Autowired
    private EventHandlingMapper mapper;

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
        validateStatus(eventHandling.getStatus());
        validateType(eventHandling.getType());
        Objects.requireNonNull(vo.getPagingVo(), "pagingVo == null");

        PageHelper.startPage(vo.getPagingVo().getPage(), vo.getPagingVo().getLimit());
        PageInfo<EventHandling> pi = PageInfo.of(mapper.selectByPage(eventHandling));
        return BeanCopyUtils.toPageList(pi, EventHandlingVo.class);
    }

    @Override
    public void updateStatus(int id, int prevStatus, int currStatus) {
        validateStatus(prevStatus);
        validateStatus(currStatus);
        mapper.updateStatus(id, prevStatus, currStatus);
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
}
