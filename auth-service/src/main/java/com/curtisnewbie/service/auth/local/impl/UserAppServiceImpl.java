package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.JsonUtils;
import com.curtisnewbie.service.auth.dao.UserApp;
import com.curtisnewbie.service.auth.infrastructure.converters.AppConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserAppMapper;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.CreateEventHandlingCmd;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import com.curtisnewbie.service.auth.remote.vo.UserRequestAppApprovalCmd;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@Service
public class UserAppServiceImpl implements LocalUserAppService {

    @Autowired
    private UserAppMapper userAppMapper;

    @Autowired
    private AppConverter cvtr;

    @Autowired
    private LocalEventHandlingService localEventHandlingService;

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

    @Override
    public void requestAppUseApproval(@NotNull UserRequestAppApprovalCmd cmd) {
        Assert.isTrue(!cmd.isInvalid(), "Command object invalid");

        try {
            localEventHandlingService.createEvent(CreateEventHandlingCmd.builder()
                    .type(EventHandlingType.REQUEST_APP_APPROVAL)
                    .body(JsonUtils.writeValueAsString(cmd))
                    .description(format("User '%s' requests access to '%s'", cmd.getUserId(), cmd.getAppId()))
                    .build());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
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
