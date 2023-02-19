package com.curtisnewbie.service.auth.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curtisnewbie.common.util.EnhancedMapper;
import com.curtisnewbie.service.auth.dao.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author yongjie.zhuang
 */
public interface UserMapper extends EnhancedMapper<User> {

    /**
     * Select * by username
     *
     * @param username username
     */
    User findByUsername(@Param("username") String username);

    /**
     * Select id by username
     *
     * @param username username
     */
    Integer findIdByUsername(@Param("username") String username);

    /**
     * Update password
     *
     * @param hashedPwd hashed password
     * @param id        id
     */
    int updatePwd(@Param("hashedPwd") String hashedPwd, @Param("id") Long id);

    /**
     * Select *
     *
     * @param id id
     */
    User findById(@Param("id") long id);

    /**
     * Select id, username, role, is_disabled, create_time, update_time, create_by, update_by
     */
    IPage<User> findUserInfoBy(Page p, @Param("u") User ue);

    /**
     * Select username
     */
    String findUsernameById(@Param("id") int id);

    /**
     * Update role, is_disabled, update_by, update_time
     */
    void updateUser(User ue);
}
