package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.module.task.vo.ListTaskByPageReqVo;
import com.curtisnewbie.module.task.vo.ListTaskByPageRespVo;
import com.curtisnewbie.service.auth.vo.ListTaskByPageReqAsVo;
import com.curtisnewbie.service.auth.vo.TaskAsVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface TaskAsConverter {

    ListTaskByPageReqVo toListTaskByPageReqAsVo(ListTaskByPageReqAsVo v);

    TaskAsVo toTaskAsVo(ListTaskByPageRespVo v);
}
