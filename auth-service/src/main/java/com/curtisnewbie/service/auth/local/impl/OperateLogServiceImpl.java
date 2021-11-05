package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.OperateLog;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.OperateLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.api.RemoteOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.curtisnewbie.service.auth.vo.MoveOperateLogToHistoryCmd;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
@DubboService(interfaceClass = RemoteOperateLogService.class)
public class OperateLogServiceImpl implements LocalOperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public void saveOperateLogInfo(@NotNull OperateLogVo operateLogVo) {
        operateLogMapper.insert(BeanCopyUtils.toType(operateLogVo, OperateLog.class));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<OperateLogVo> findOperateLogInfoInPages(@NotNull PagingVo pagingVo) {
        Objects.requireNonNull(pagingVo.getPage());
        Objects.requireNonNull(pagingVo.getLimit());
        PageHelper.startPage(pagingVo.getPage(), pagingVo.getLimit());
        PageInfo<OperateLog> pi = PageInfo.of(operateLogMapper.selectBasicInfo());
        return BeanCopyUtils.toPageList(pi, OperateLogVo.class);
    }

    @Override
    public void moveRecordsToHistory(@NotNull MoveOperateLogToHistoryCmd cmd) {
        // validate the command object
        cmd.validate();

        final LocalDateTime before = cmd.before();
        final int batchLimit = cmd.batchSize();

        // records are moved, no need to change the page number
        final PagingVo paging = new PagingVo().ofLimit(batchLimit).ofPage(1);

        PageInfo<Integer> ids = findIdsBeforeDateByPage(paging, before);

        // count of records being moved
        int count = 0;

        // while has next page
        while (moveRecordsToHistory(ids)) {

            count += ids.getList().size();

            // violated maxCount, end the loop
            if (cmd.isMaxCountViolated(count))
                break;

            ids = findIdsBeforeDateByPage(paging, before);
        }
        log.info("Found and moved {} operate_log records before '{}' to operate_log_history", count,
                before);
    }

    // ----------------------- private methods ----------------------

    /**
     * Move records to operate_log_history table
     *
     * @return has next page
     */
    private boolean moveRecordsToHistory(PageInfo<Integer> idsPi) {
        List<Integer> ids = idsPi.getList();
        if (ids.isEmpty())
            return false;
        operateLogMapper.copyToHistory(ids);
        operateLogMapper.deleteByIds(ids);
        return true;
    }

    private PageInfo<Integer> findIdsBeforeDateByPage(PagingVo paging, LocalDateTime date) {
        Objects.requireNonNull(paging);
        Objects.requireNonNull(date);

        PageHelper.startPage(paging.getPage(), paging.getLimit());
        return PageInfo.of(operateLogMapper.selectIdsBeforeDate(date));
    }
}
