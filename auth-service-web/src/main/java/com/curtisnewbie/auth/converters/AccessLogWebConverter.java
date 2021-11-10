package com.curtisnewbie.auth.converters;

import com.curtisnewbie.auth.vo.AccessLogInfoWebVo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface AccessLogWebConverter {

    AccessLogInfoWebVo toWebVo(AccessLogInfoVo vo);

}
