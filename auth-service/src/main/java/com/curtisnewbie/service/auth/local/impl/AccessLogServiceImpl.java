package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.AccessLogEntity;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.AccessLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Service
@Transactional
@DubboService(interfaceClass = RemoteAccessLogService.class)
public class AccessLogServiceImpl implements LocalAccessLogService {

    @Autowired
    private AccessLogMapper m;

    @Override
    public void save(AccessLogInfoVo accessLogVo) {
        m.insert(BeanCopyUtils.toType(accessLogVo, AccessLogEntity.class));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<AccessLogInfoVo> findAccessLogInfoByPage(PagingVo paging) {
        Objects.requireNonNull(paging);
        PageHelper.startPage(paging.getPage(), paging.getLimit());
        List<AccessLogEntity> list = m.selectAllBasicInfo();
        return BeanCopyUtils.toPageList(PageInfo.of(list), AccessLogInfoVo.class);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public @NotNull PageInfo<Integer> findIdsBeforeDateByPage(@NotNull PagingVo paging, @NotNull Date date) {
        Objects.requireNonNull(paging);
        Objects.requireNonNull(date);

        PageHelper.startPage(paging.getPage(), paging.getLimit());
        return PageInfo.of(m.selectIdsBeforeDate(date));
    }

    @Override
    public void moveRecordsToHistory(@NotNull List<Integer> ids) {
        if (ids.isEmpty())
            return;
        m.copyToHistory(ids);
        m.deleteByIds(ids);
    }
}
