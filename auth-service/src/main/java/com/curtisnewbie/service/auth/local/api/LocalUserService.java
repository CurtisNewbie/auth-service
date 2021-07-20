package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.dao.UserEntity;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.exception.*;


/**
 * Service related to user table
 *
 * @author yongjie.zhuang
 */
public interface LocalUserService extends RemoteUserService {

    /**
     * Find user by username
     *
     * @throws UsernameNotFoundException user with given username is not found
     */
    UserEntity loadUserByUsername(String username) throws UsernameNotFoundException;

}