package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.remote.vo.AppVo;
import com.curtisnewbie.service.auth.vo.AppWebVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface AppWebConverter {

    AppWebVo toWebVo(AppVo av);
}
