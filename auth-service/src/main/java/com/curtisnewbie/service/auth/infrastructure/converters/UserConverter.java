package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Converter for User
 *
 * @author yongjie.zhuang
 */
@Mapper
public interface UserConverter {

    UserConverter converter = Mappers.getMapper(UserConverter.class);

    User toDo(UserVo vo);

    UserVo toVo(User udo);
}
