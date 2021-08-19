package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.OperateLogEntity;
import com.curtisnewbie.service.auth.dao.OperateLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.api.RemoteOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@DubboService(interfaceClass = RemoteOperateLogService.class)
public class OperateLogServiceImpl implements LocalOperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public void saveOperateLogInfo(@NotNull OperateLogVo operateLogVo) {
        operateLogMapper.insert(BeanCopyUtils.toType(operateLogVo, OperateLogEntity.class));
    }

    @Override
    public PageInfo<OperateLogVo> findOperateLogInfoInPages(@NotNull PagingVo pagingVo) {
        Objects.requireNonNull(pagingVo.getPage());
        Objects.requireNonNull(pagingVo.getLimit());
        PageHelper.startPage(pagingVo.getPage(), pagingVo.getLimit());
        PageInfo<OperateLogEntity> pi = PageInfo.of(operateLogMapper.selectBasicInfo());
        return BeanCopyUtils.toPageList(pi, OperateLogVo.class);
    }
}