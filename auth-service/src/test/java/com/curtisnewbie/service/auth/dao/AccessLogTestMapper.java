package com.curtisnewbie.service.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author yongjie.zhuang
 */
@Mapper
public interface AccessLogTestMapper {

    @Select("SELECT id FROM access_log WHERE ip_address = #{ip}")
    Integer findIdByIp(@Param("ip") String ip);

}
