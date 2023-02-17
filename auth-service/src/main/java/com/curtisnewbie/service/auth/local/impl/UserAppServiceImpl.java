package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curtisnewbie.service.auth.dao.UserApp;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserAppMapper;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.curtisnewbie.common.util.BeanCopyUtils.*;
import static java.util.Objects.nonNull;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
public class UserAppServiceImpl implements LocalUserAppService {

    @Autowired
    private UserAppMapper userAppMapper;

    @Override
    public boolean isUserAllowedToUseApp(int userId, @NotEmpty String appName) {
        return Objects.equals(userAppMapper.selectOneIfUserIsAllowed(userId, appName), 1);
    }

    @Override
    public List<AppBriefVo> getAppsPermittedForUser(int userId) {
        return toTypeList(userAppMapper.getAppsPermittedForUser(userId), AppBriefVo.class);
    }

    @Override
    @Transactional
    public void updateUserApp(@NotNull UpdateUserAppReqCmd cmd) {
        cmd.validate();

        final int userId = cmd.getUserId();

        // clear all apps first
        userAppMapper.clearAppsForUser(userId);

        // insert the new apps
        if (cmd.hasAppToAssign())
            userAppMapper.setAppsForUser(userId, cmd.getAppIdList());
    }

    @Override
    @Transactional
    public void addUserApp(int userId, int appId, @NotEmpty String createdBy) {
        QueryWrapper<UserApp> condition = new QueryWrapper<>();
        condition
                .select("app_id") // just select random field, so that the object is not null when the record exists
                .eq("user_id", userId)
                .eq("app_id", appId);

        // record exists already
        if (nonNull(userAppMapper.selectOne(condition)))
            return;

        UserApp ua = new UserApp();
        ua.setAppId(appId);
        ua.setUserId(userId);
        ua.setCreateBy(createdBy);
        ua.setCreateTime(LocalDateTime.now());

        userAppMapper.insert(ua);
    }
}
