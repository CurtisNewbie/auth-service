package com.curtisnewbie.service.auth.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curtisnewbie.service.auth.dao.OperateLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Mapper for operate_log
 *
 * @author yongjie.zhuang
 */
public interface OperateLogMapper extends BaseMapper<OperateLogEntity> {

    OperateLogEntity selectByPrimaryKey(Integer id);

    List<OperateLogEntity> selectAll();

    int updateByPrimaryKey(OperateLogEntity record);

    /**
     * select operate_name, operate_desc, operate_time, operate_param, username, user_id
     */
    List<OperateLogEntity> selectBasicInfo();

    List<Integer> selectIdsBeforeDate(@Param("date") Date date);

    void copyToHistory(@Param("ids") List<Integer> ids);

    void deleteByIds(@Param("ids") List<Integer> ids);
}