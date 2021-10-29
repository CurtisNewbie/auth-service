package com.curtisnewbie.service.auth.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curtisnewbie.service.auth.dao.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.Date;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
public interface UserMapper extends BaseMapper<UserEntity> {

    /**
     * Select * by username
     *
     * @param username username
     */
    UserEntity findByUsername(@Param("username") String username);

    /**
     * Select id by username
     *
     * @param username username
     */
    Integer findIdByUsername(@Param("username") String username);

    /**
     * Count number of administrators
     */
    int countAdmin();

    /**
     * Update password
     *
     * @param hashedPwd hashed password
     * @param id        id
     */
    void updatePwd(@Param("hashedPwd") String hashedPwd, @Param("id") Long id);

    /**
     * Select id, username, role of normal users
     */
    List<UserEntity> findNormalUserInfoList();

    /**
     * Select id, username, role, is_disabled of all users
     */
    List<UserEntity> findAllUserInfoList();

    /**
     * Select *
     *
     * @param id id
     */
    UserEntity findById(@Param("id") long id);

    /**
     * Select id, username, role, is_disabled, create_time, update_time, create_by, update_by
     */
    List<UserEntity> findUserInfoBy(UserEntity ue);

    /**
     * Select username
     */
    String findUsernameById(@Param("id") int id);

    /**
     * Update role, is_disabled, update_by, update_time
     */
    void updateUser(UserEntity ue);

    /**
     * Move disabled user to deleted_user
     */
    int moveDisabledUser(@Param("id") int id, @Param("deletedBy") String deletedBy);

    /**
     * Delete record
     */
    void deleteUser(@Param("id") int id);
}
