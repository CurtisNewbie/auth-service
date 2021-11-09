package com.curtisnewbie.auth.converters;

import com.curtisnewbie.auth.vo.ListTaskHistoryByPageReqWebVo;
import com.curtisnewbie.auth.vo.TaskHistoryWebVo;
import com.curtisnewbie.module.task.vo.ListTaskHistoryByPageReqVo;
import com.curtisnewbie.module.task.vo.ListTaskHistoryByPageRespVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface TaskHistoryAsConverter {

    ListTaskHistoryByPageReqVo toListTaskHistoryByPageReqVo(ListTaskHistoryByPageReqWebVo v);

    TaskHistoryWebVo toTaskHistoryWebVo(ListTaskHistoryByPageRespVo v);
}
