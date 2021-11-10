package com.curtisnewbie.auth.converters;

import com.curtisnewbie.auth.vo.AppWebVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface AppWebConverter {

    AppWebVo toWebVo(AppVo av);
}
