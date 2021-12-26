package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.module.task.vo.ListTaskHistoryByPageReqVo;
import com.curtisnewbie.module.task.vo.ListTaskHistoryByPageRespVo;
import com.curtisnewbie.service.auth.vo.ListTaskHistoryByPageReqWebVo;
import com.curtisnewbie.service.auth.vo.TaskHistoryWebVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface TaskHistoryAsConverter {

    ListTaskHistoryByPageReqVo toListTaskHistoryByPageReqVo(ListTaskHistoryByPageReqWebVo v);

    TaskHistoryWebVo toTaskHistoryWebVo(ListTaskHistoryByPageRespVo v);
}
