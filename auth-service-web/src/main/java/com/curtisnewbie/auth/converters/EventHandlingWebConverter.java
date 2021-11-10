package com.curtisnewbie.auth.converters;

import com.curtisnewbie.auth.vo.EventHandlingWebVo;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface EventHandlingWebConverter {

    EventHandlingWebVo toWebVo(EventHandlingVo v);
}
