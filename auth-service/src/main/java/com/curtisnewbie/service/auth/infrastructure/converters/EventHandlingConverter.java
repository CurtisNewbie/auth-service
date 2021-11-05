package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.EventHandling;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.remote.vo.FindEventHandlingByPageReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Converter for EventHandling
 *
 * @author yongjie.zhuang
 */
@Mapper
public interface EventHandlingConverter {

    EventHandlingConverter converter = Mappers.getMapper(EventHandlingConverter.class);

    EventHandling toDo(EventHandlingVo vo);

    EventHandling toDo(FindEventHandlingByPageReqVo vo);

    EventHandlingVo toVo(EventHandling ehdo);


}
