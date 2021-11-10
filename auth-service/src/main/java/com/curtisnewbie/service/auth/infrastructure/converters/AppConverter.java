package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.App;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface AppConverter {

    AppVo toVo(App app);

    AppBriefVo toBriefVo(App app);
}
