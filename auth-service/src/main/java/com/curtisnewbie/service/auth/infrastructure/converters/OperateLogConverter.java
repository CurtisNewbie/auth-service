package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.OperateLog;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Converter for OperateLog
 * @author yongjie.zhuang
 */
@Mapper
public interface OperateLogConverter {

    OperateLogConverter converter = Mappers.getMapper(OperateLogConverter.class);

    OperateLog toDo(OperateLogVo vo);
}

