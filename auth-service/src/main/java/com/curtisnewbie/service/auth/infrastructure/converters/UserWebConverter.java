package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import com.curtisnewbie.service.auth.vo.UserInfoWebVo;
import com.curtisnewbie.service.auth.vo.UserWebVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface UserWebConverter {

    UserWebVo toWebVo(UserVo uv);

    UserInfoWebVo toWebInfoVo(UserInfoVo uiv);
}
