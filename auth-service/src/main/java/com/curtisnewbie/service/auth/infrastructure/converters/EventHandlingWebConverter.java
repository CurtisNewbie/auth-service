package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import com.curtisnewbie.service.auth.vo.EventHandlingWebVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface EventHandlingWebConverter {

    EventHandlingWebVo toWebVo(EventHandlingVo v);
}
