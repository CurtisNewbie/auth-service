package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.service.auth.infrastructure.converters.AppConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserAppMapper;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@DubboService(interfaceClass = RemoteUserAppService.class)
public class UserAppServiceImpl implements LocalUserAppService {

    @Autowired
    private UserAppMapper userAppMapper;

    @Autowired
    private AppConverter cvtr;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserAllowedToUseApp(int userId, @NotEmpty String appName) {
        return Objects.equals(userAppMapper.selectOneIfUserIsAllowed(userId, appName), 1);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<AppBriefVo> getAppsPermittedForUser(int userId) {
        return BeanCopyUtils.mapTo(userAppMapper.getAppsPermittedForUser(userId), cvtr::toBriefVo);
    }

    @Override
    public void updateUserApp(@NotNull UpdateUserAppReqCmd cmd) {
        cmd.validate();

        final int userId = cmd.getUserId();

        // clear all apps first
        userAppMapper.clearAppsForUser(userId);

        // insert the new apps
        if (cmd.hasAppToAssign())
            userAppMapper.setAppsForUser(userId, cmd.getAppIdList());
    }
}
