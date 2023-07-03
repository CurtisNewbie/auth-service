package com.curtisnewbie.service.auth.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curtisnewbie.service.auth.dao.AccessLog;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

public interface AccessLogMapper extends BaseMapper<AccessLog> {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table access_log
     *
     * @mbg.generated Sun May 23 23:31:27 CST 2021
     */
    AccessLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table access_log
     *
     * @mbg.generated Sun May 23 23:31:27 CST 2021
     */
    int updateByPrimaryKey(AccessLog record);

    IPage<AccessLog> selectAllBasicInfo(Page p);

    IPage<Integer> selectIdsBeforeDate(Page p, @Param("date") LocalDateTime date);
}