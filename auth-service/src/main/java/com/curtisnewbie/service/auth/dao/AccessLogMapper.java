package com.curtisnewbie.service.auth.dao;

import java.util.List;

public interface AccessLogMapper {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table access_log
     *
     * @mbg.generated Sun May 23 23:31:27 CST 2021
     */
    int insert(AccessLogEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table access_log
     *
     * @mbg.generated Sun May 23 23:31:27 CST 2021
     */
    AccessLogEntity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table access_log
     *
     * @mbg.generated Sun May 23 23:31:27 CST 2021
     */
    List<AccessLogEntity> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table access_log
     *
     * @mbg.generated Sun May 23 23:31:27 CST 2021
     */
    int updateByPrimaryKey(AccessLogEntity record);

    List<AccessLogEntity> selectAllBasicInfo();
}