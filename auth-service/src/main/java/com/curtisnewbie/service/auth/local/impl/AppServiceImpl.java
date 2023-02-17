package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.App;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.AppMapper;
import com.curtisnewbie.service.auth.local.api.LocalAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@Service
public class AppServiceImpl implements LocalAppService {

    @Autowired
    private AppMapper appMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageableList<AppVo> getAllAppInfo(@NotNull PagingVo pagingVo) {
        IPage<App> ipg = appMapper.selectAll(PagingUtil.forPage(pagingVo));
        return PagingUtil.toPageableList(ipg, b -> BeanCopyUtils.toType(b, AppVo.class));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AppBriefVo> getAllAppBriefInfo() {
        return BeanCopyUtils.toTypeList(appMapper.selectAllBriefInfo(), AppBriefVo.class);
    }

    @Override
    public Integer getAppIdByName(String appName) {
        return appMapper.selectAndConvert(new LambdaQueryWrapper<App>()
                .select(App::getId)
                .eq(App::getName, appName)
                .last("limit 1"), App::getId);
    }
}
