package com.curtisnewbie.service.auth.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper
public interface UserAppTestMapper {

    @Insert("INSERT INTO user_app (user_id, app_id) VALUES (#{userId}, #{appId})")
    void insertUserAppRecord(UserApp userApp);

}
