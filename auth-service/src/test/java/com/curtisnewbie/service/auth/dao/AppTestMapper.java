package com.curtisnewbie.service.auth.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper
public interface AppTestMapper {

    @Insert("INSERT INTO app (id, name) VALUES (#{id}, #{name})")
    void insertAppRecord(App app);

}
