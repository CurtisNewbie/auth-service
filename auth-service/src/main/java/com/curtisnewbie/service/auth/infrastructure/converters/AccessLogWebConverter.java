package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.vo.AccessLogInfoWebVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface AccessLogWebConverter {

    AccessLogInfoWebVo toWebVo(AccessLogInfoVo vo);

}
