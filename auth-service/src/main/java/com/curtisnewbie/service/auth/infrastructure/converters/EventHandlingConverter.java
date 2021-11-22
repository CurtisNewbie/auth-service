package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.EventHandling;
import com.curtisnewbie.service.auth.remote.vo.CreateEventHandlingCmd;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import org.mapstruct.Mapper;

/**
 * Converter for EventHandling
 *
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface EventHandlingConverter {

    EventHandling toDo(CreateEventHandlingCmd cmd);

    EventHandling toDo(EventHandlingVo vo);

    EventHandling toDo(FindEventHandlingByPageReqVo vo);

    EventHandlingVo toVo(EventHandling ehdo);


}
