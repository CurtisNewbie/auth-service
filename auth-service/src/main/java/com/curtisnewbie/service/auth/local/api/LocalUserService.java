package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.exception.*;

import javax.validation.constraints.NotEmpty;


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
    User loadUserByUsername(@NotEmpty String username) throws UsernameNotFoundException;


}
