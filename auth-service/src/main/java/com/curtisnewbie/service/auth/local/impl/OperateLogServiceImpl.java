package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.OperateLog;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.OperateLogMapper;
import com.curtisnewbie.service.auth.local.api.OperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import static com.curtisnewbie.common.util.BeanCopyUtils.*;
import static com.curtisnewbie.common.util.PagingUtil.forPage;

/**
 * @author yongjie.zhuang
 */
@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public void saveOperateLogInfo(@NotNull OperateLogVo operateLogVo) {
        operateLogMapper.insert(toType(operateLogVo, OperateLog.class));
    }

    @Override
    public PageableList<OperateLogVo> findOperateLogInfoInPages(@NotNull PagingVo pagingVo) {
        IPage<OperateLog> ipg = operateLogMapper.selectBasicInfo(forPage(pagingVo));
        return PagingUtil.toPageableList(ipg, o -> toType(o, OperateLogVo.class));
    }
}
