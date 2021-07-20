package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.AccessLogEntity;
import com.curtisnewbie.service.auth.dao.AccessLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Service
@DubboService(interfaceClass = RemoteAccessLogService.class)
public class AccessLogServiceImpl implements LocalAccessLogService {

    @Autowired
    private AccessLogMapper m;

    @Override
    public void save(AccessLogVo accessLogVo) {
        accessLogVo.setId(null);
        m.insert(BeanCopyUtils.toType(accessLogVo, AccessLogEntity.class));
    }

    @Override
    public PageInfo<AccessLogInfoVo> findAccessLogInfoByPage(PagingVo paging) {
        Objects.requireNonNull(paging);
        PageHelper.startPage(paging.getPage(), paging.getLimit());
        List<AccessLogEntity> list = m.selectAllBasicInfo();
        return BeanCopyUtils.toPageList(PageInfo.of(list), AccessLogInfoVo.class);
    }
}
