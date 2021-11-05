package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.AccessLog;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.AccessLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.vo.MoveAccessLogToHistoryCmd;
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

import static com.curtisnewbie.service.auth.infrastructure.converters.AccessLogConverter.converter;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
@DubboService(interfaceClass = RemoteAccessLogService.class)
public class AccessLogServiceImpl implements LocalAccessLogService {

    @Autowired
    private AccessLogMapper m;

    @Override
    public void save(AccessLogInfoVo accessLogVo) {
        m.insert(converter.toDo(accessLogVo));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageInfo<AccessLogInfoVo> findAccessLogInfoByPage(PagingVo paging) {
        Objects.requireNonNull(paging);
        PageHelper.startPage(paging.getPage(), paging.getLimit());
        List<AccessLog> list = m.selectAllBasicInfo();
        return BeanCopyUtils.toPageList(PageInfo.of(list), AccessLogInfoVo.class);
    }

    @Override
    public void moveRecordsToHistory(@NotNull MoveAccessLogToHistoryCmd cmd) {
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
        log.info("Found and moved {} access_log records before '{}', moving them to access_log_history",
                ids.getList().size(), before);
    }

    // ---------------------- private methods ---------------------------


    /**
     * Find ids of records where the access_date is before the given date
     */
    private PageInfo<Integer> findIdsBeforeDateByPage(PagingVo paging, LocalDateTime date) {
        Objects.requireNonNull(paging);
        Objects.requireNonNull(date);

        PageHelper.startPage(paging.getPage(), paging.getLimit());
        return PageInfo.of(m.selectIdsBeforeDate(date));
    }


    /**
     * Move the records to access_log_history
     */
    private boolean moveRecordsToHistory(PageInfo<Integer> idsPi) {
        List<Integer> ids = idsPi.getList();
        if (ids.isEmpty())
            return false;

        m.copyToHistory(ids);
        m.deleteByIds(ids);
        return true;
    }
}
