package com.curtisnewbie.service.auth.dao;

import java.util.List;

/**
 * Mapper for operate_log
 *
 * @author yongjie.zhuang
 */
public interface OperateLogMapper {

    int insert(OperateLogEntity record);

    OperateLogEntity selectByPrimaryKey(Integer id);

    List<OperateLogEntity> selectAll();

    int updateByPrimaryKey(OperateLogEntity record);

    /**
     * select operate_name, operate_desc, operate_time, operate_param, username, user_id
     */
    List<OperateLogEntity> selectBasicInfo();
}