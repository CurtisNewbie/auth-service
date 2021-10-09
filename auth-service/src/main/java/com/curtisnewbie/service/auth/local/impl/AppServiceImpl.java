package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.App;
import com.curtisnewbie.service.auth.dao.AppMapper;
import com.curtisnewbie.service.auth.local.api.LocalAppService;
import com.curtisnewbie.service.auth.remote.api.RemoteAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.curtisnewbie.common.util.BeanCopyUtils.pageInfoOf;
import static com.curtisnewbie.common.util.BeanCopyUtils.toTypeList;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@DubboService(interfaceClass = RemoteAppService.class)
public class AppServiceImpl implements LocalAppService {

    @Autowired
    private AppMapper appMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<AppVo> getAllAppInfo(@NotNull PagingVo pagingVo) {
        PageHelper.startPage(pagingVo.getPage(), pagingVo.getLimit());
        return pageInfoOf(
                appMapper.selectAll(),
                AppVo.class
        );
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AppBriefVo> getAllAppBriefInfo() {
        return toTypeList(
                appMapper.selectAllBriefInfo(),
                AppBriefVo.class
        );
    }
}
