package com.curtisnewbie.auth.converters;

import com.curtisnewbie.auth.vo.OperateLogWebVo;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface OperateLogWebConverter {

    OperateLogWebVo toWebVo(OperateLogVo ov);
}
