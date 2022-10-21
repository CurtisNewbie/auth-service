package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.AccessLog;
import com.curtisnewbie.service.auth.infrastructure.converters.AccessLogConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.AccessLogMapper;
import com.curtisnewbie.service.auth.local.api.LocalAccessLogService;
import com.curtisnewbie.service.auth.local.vo.cmd.MoveAccessLogToHistoryCmd;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
public class AccessLogServiceImpl implements LocalAccessLogService {

    @Autowired
    private AccessLogMapper m;

    @Autowired
    private AccessLogConverter converter;

    @Override
    public void save(AccessLogInfoVo accessLogVo) {
        m.insert(converter.toDo(accessLogVo));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageableList<AccessLogInfoVo> findAccessLogInfoByPage(@NotNull PagingVo paging) {
        IPage<AccessLog> list = m.selectAllBasicInfo(PagingUtil.forPage(paging));
        return PagingUtil.toPageableList(list, v -> BeanCopyUtils.toType(v, AccessLogInfoVo.class));
    }

    // ---------------------- private methods ---------------------------


    /**
     * Find ids of records where the access_date is before the given date
     */
    private IPage<Integer> findIdsBeforeDateByPage(PagingVo paging, LocalDateTime date) {
        return m.selectIdsBeforeDate(PagingUtil.forPage(paging), date);
    }

}
