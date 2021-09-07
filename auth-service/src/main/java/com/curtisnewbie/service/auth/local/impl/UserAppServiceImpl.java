package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.*;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import com.curtisnewbie.service.auth.remote.vo.GetAppsPermittedForUserReqVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static com.curtisnewbie.common.util.BeanCopyUtils.pageInfoOf;
import static com.curtisnewbie.common.util.BeanCopyUtils.toTypeList;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@DubboService(interfaceClass = RemoteUserAppService.class)
public class UserAppServiceImpl implements LocalUserAppService {

    @Autowired
    private UserAppMapper userAppMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserAllowedToUseApp(int userId, @NotEmpty String appName) {
        return Objects.equals(userAppMapper.selectOneIfUserIsAllowed(userId, appName), 1);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<AppBriefVo> getAppsPermittedForUser(int userId) {
        return toTypeList(
                userAppMapper.getAppsPermittedForUser(userId),
                AppBriefVo.class
        );
    }

    @Override
    public void updateUserApp(@NotNull UpdateUserAppReqVo vo) {
        // clear all apps first
        userAppMapper.clearAppsForUser(vo.getUserId());

        // insert the new apps
        if (!vo.getAppIdList().isEmpty())
            userAppMapper.setAppsForUser(vo.getUserId(), vo.getAppIdList());
    }
}
