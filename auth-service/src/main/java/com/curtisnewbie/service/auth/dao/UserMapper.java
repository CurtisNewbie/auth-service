package com.curtisnewbie.service.auth.dao;

import com.curtisnewbie.service.auth.remote.vo.FindUserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
public interface UserMapper {

    /**
     * Select * by username
     *
     * @param username username
     */
    UserEntity findByUsername(@Param("username") String username);

    /**
     * Insert user
     */
    void insert(UserEntity userEntity);

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
     * Update role, is_disabled
     */
    void updateUser(UserEntity ue);
}
