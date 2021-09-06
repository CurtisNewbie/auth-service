package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.service.auth.dao.UserAppMapper;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
public class LocalUserAppServiceImpl implements LocalUserAppService {

    @Autowired
    private UserAppMapper userAppMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserAllowedToUseApp(int userId, @NotEmpty String appName) {
        return Objects.equals(userAppMapper.selectOneIfUserIsAllowed(userId, appName), 1);
    }
}
