package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.AccessLog;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.AccessLogMapper;
import com.curtisnewbie.service.auth.local.api.AccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * @author yongjie.zhuang
 */
@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogMapper m;

    @Override
    public void save(AccessLogInfoVo accessLogVo) {
        m.insert(BeanCopyUtils.toType(accessLogVo, AccessLog.class));
    }

    @Override
    public PageableList<AccessLogInfoVo> findAccessLogInfoByPage(@NotNull PagingVo paging) {
        IPage<AccessLog> list = m.selectAllBasicInfo(PagingUtil.forPage(paging));
        return PagingUtil.toPageableList(list, v -> BeanCopyUtils.toType(v, AccessLogInfoVo.class));
    }

}
