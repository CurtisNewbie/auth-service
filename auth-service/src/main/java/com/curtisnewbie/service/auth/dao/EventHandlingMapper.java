package com.curtisnewbie.service.auth.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface EventHandlingMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table event_handling
     *
     * @mbg.generated Sun Aug 29 23:47:48 CST 2021
     */
    int insert(EventHandling record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table event_handling
     *
     * @mbg.generated Sun Aug 29 23:47:48 CST 2021
     */
    EventHandling selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table event_handling
     *
     * @mbg.generated Sun Aug 29 23:47:48 CST 2021
     */
    List<EventHandling> selectAll();

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table event_handling
     *
     * @mbg.generated Sun Aug 29 23:47:48 CST 2021
     */
    int updateByPrimaryKey(EventHandling record);

    /**
     * Select * by status, type
     */
    List<EventHandling> selectByPage(EventHandling eventHandling);

    int updateHandlingResult(@Param("id") int id, @Param("prevStatus") int prevStatus, @Param("currStatus") int currStatus
            , @Param("handlerId") int handlerId, @Param("handleTime") Date handleTime);

    Integer selectTypeById(int id);
}