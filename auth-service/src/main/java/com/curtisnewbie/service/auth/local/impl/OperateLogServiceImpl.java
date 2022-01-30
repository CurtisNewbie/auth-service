package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.OperateLog;
import com.curtisnewbie.service.auth.infrastructure.converters.OperateLogConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.OperateLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.curtisnewbie.service.auth.local.vo.cmd.MoveOperateLogToHistoryCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static com.curtisnewbie.common.util.PagingUtil.forPage;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
public class OperateLogServiceImpl implements LocalOperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Autowired
    private OperateLogConverter cvtr;


    @Override
    public void saveOperateLogInfo(@NotNull OperateLogVo operateLogVo) {
        operateLogMapper.insert(cvtr.toDo(operateLogVo));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageablePayloadSingleton<List<OperateLogVo>> findOperateLogInfoInPages(@NotNull PagingVo pagingVo) {
        IPage<OperateLog> ipg = operateLogMapper.selectBasicInfo(forPage(pagingVo));
        return PagingUtil.toPageList(ipg, cvtr::toVo);
    }

    @Override
    public void moveRecordsToHistory(@NotNull MoveOperateLogToHistoryCmd cmd) {
        // validate the command object
        cmd.validate();

        final LocalDateTime before = cmd.before();
        final int batchLimit = cmd.batchSize();

        // records are moved, no need to change the page number
        final PagingVo paging = new PagingVo().ofLimit(batchLimit).ofPage(1);

        IPage<Integer> page = findIdsBeforeDateByPage(paging, before);

        // count of records being moved
        int count = 0;

        // while has next page
        while (moveRecordsToHistory(page.getRecords())) {

            count += page.getRecords().size();

            // violated maxCount, end the loop
            if (cmd.isMaxCountViolated(count))
                break;

            page = findIdsBeforeDateByPage(paging, before);
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
    private boolean moveRecordsToHistory(List<Integer> ids) {
        if (ids.isEmpty())
            return false;
        operateLogMapper.copyToHistory(ids);
        operateLogMapper.deleteByIds(ids);
        return true;
    }

    private IPage<Integer> findIdsBeforeDateByPage(PagingVo paging, LocalDateTime date) {
        return operateLogMapper.selectIdsBeforeDate(forPage(paging), date);
    }
}
