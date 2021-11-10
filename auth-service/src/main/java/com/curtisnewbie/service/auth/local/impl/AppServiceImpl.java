package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.App;
import com.curtisnewbie.service.auth.infrastructure.converters.AppConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.AppMapper;
import com.curtisnewbie.service.auth.local.api.LocalAppService;
import com.curtisnewbie.service.auth.remote.api.RemoteAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Transactional
@DubboService(interfaceClass = RemoteAppService.class)
public class AppServiceImpl implements LocalAppService {

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private AppConverter cvtr;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageablePayloadSingleton<List<AppVo>> getAllAppInfo(@NotNull PagingVo pagingVo) {
        IPage<App> ipg = appMapper.selectAll(PagingUtil.forPage(pagingVo));
        return PagingUtil.toPageList(ipg, cvtr::toVo);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AppBriefVo> getAllAppBriefInfo() {
        return BeanCopyUtils.mapTo(appMapper.selectAllBriefInfo(), cvtr::toBriefVo);
    }
}
