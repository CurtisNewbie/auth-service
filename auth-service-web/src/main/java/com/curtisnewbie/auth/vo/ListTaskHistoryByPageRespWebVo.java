package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.module.task.vo.ListTaskHistoryByPageRespVo;
import com.curtisnewbie.module.task.vo.TaskHistoryVo;
import lombok.Data;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Data
public class ListTaskHistoryByPageRespWebVo extends PageableVo {

    private List<TaskHistoryWebVo> list;
}
