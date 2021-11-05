package com.curtisnewbie.service.auth.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curtisnewbie.service.auth.dao.OperateLog;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Mapper for operate_log
 *
 * @author yongjie.zhuang
 */
public interface OperateLogMapper extends BaseMapper<OperateLog> {

    OperateLog selectByPrimaryKey(Integer id);

    List<OperateLog> selectAll();

    int updateByPrimaryKey(OperateLog record);

    /**
     * select operate_name, operate_desc, operate_time, operate_param, username, user_id
     */
    List<OperateLog> selectBasicInfo();

    List<Integer> selectIdsBeforeDate(@Param("date") LocalDateTime date);

    void copyToHistory(@Param("ids") List<Integer> ids);

    void deleteByIds(@Param("ids") List<Integer> ids);
}