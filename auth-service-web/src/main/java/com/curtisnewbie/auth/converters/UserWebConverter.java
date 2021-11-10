package com.curtisnewbie.auth.converters;

import com.curtisnewbie.auth.vo.UserInfoWebVo;
import com.curtisnewbie.auth.vo.UserWebVo;
import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface UserWebConverter {

    UserWebVo toWebVo(UserVo uv);

    UserInfoWebVo toWebInfoVo(UserInfoVo uiv);
}
