package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.OperateLog;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.OperateLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.local.vo.cmd.MoveOperateLogToHistoryCmd;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static com.curtisnewbie.common.util.BeanCopyUtils.*;
import static com.curtisnewbie.common.util.PagingUtil.forPage;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
public class OperateLogServiceImpl implements LocalOperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void saveOperateLogInfo(@NotNull OperateLogVo operateLogVo) {
        operateLogMapper.insert(toType(operateLogVo, OperateLog.class));
    }

    @Override
    public PageableList<OperateLogVo> findOperateLogInfoInPages(@NotNull PagingVo pagingVo) {
        IPage<OperateLog> ipg = operateLogMapper.selectBasicInfo(forPage(pagingVo));
        return PagingUtil.toPageableList(ipg, o -> toType(o, OperateLogVo.class));
    }

    private IPage<Integer> findIdsBeforeDateByPage(PagingVo paging, LocalDateTime date) {
        return operateLogMapper.selectIdsBeforeDate(forPage(paging), date);
    }
}
