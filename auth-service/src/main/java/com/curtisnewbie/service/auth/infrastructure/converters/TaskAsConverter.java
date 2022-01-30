package com.curtisnewbie.service.auth.infrastructure.converters;

import com.curtisnewbie.module.task.vo.ListTaskByPageReqVo;
import com.curtisnewbie.module.task.vo.ListTaskByPageRespVo;
import com.curtisnewbie.service.auth.web.open.api.vo.ListTaskByPageReqAsVo;
import com.curtisnewbie.service.auth.web.open.api.vo.TaskAsVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface TaskAsConverter {

    ListTaskByPageReqVo toListTaskByPageReqAsVo(ListTaskByPageReqAsVo v);

    TaskAsVo toTaskAsVo(ListTaskByPageRespVo v);
}
