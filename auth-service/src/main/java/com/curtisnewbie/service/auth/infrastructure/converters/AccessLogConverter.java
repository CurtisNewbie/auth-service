package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.AccessLog;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.mapstruct.Mapper;

/**
 * Converter for AccessLog
 *
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface AccessLogConverter {

    AccessLog toDo(AccessLogInfoVo vo);

    AccessLogInfoVo toVo(AccessLog al);


}
