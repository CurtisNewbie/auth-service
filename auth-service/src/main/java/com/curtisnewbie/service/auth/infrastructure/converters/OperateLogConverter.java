package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.OperateLog;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.mapstruct.Mapper;

/**
 * Converter for OperateLog
 *
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface OperateLogConverter {

    OperateLog toDo(OperateLogVo vo);

    OperateLogVo toVo(OperateLog ol);
}

