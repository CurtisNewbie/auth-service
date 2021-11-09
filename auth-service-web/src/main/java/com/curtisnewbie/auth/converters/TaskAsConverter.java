package com.curtisnewbie.auth.converters;

import com.curtisnewbie.auth.vo.ListTaskByPageReqAsVo;
import com.curtisnewbie.auth.vo.TaskAsVo;
import com.curtisnewbie.module.task.vo.ListTaskByPageReqVo;
import com.curtisnewbie.module.task.vo.ListTaskByPageRespVo;
import org.mapstruct.Mapper;

/**
 * @author yongjie.zhuang
 */
@Mapper(componentModel = "spring")
public interface TaskAsConverter {

    ListTaskByPageReqVo toListTaskByPageReqAsVo(ListTaskByPageReqAsVo v);

    TaskAsVo toTaskAsVo(ListTaskByPageRespVo v);
}
