package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.curtisnewbie.service.auth.vo.OperateLogWebVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface OperateLogWebConverter {

    OperateLogWebVo toWebVo(OperateLogVo ov);
}
