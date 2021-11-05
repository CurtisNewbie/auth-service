package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.AccessLog;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Converter for AccessLog
 *
 * @author yongjie.zhuang
 */
@Mapper
public interface AccessLogConverter {

    AccessLogConverter converter = Mappers.getMapper(AccessLogConverter.class);

    AccessLog toDo(AccessLogInfoVo vo);
}
