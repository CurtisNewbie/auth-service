package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.mapstruct.Mapper;

/**
 * Converter for User
 *
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    User toDo(UserVo vo);

    UserVo toVo(User udo);

    UserInfoVo toInfoVo(User u);
}
