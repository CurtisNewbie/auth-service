package com.curtisnewbie.service.auth.vo;

import com.curtisnewbie.common.vo.PageableVo;
import lombok.Data;

import java.util.List;

/**
 * Response vo for listing tasks in pages
 *
 * @author yongjie.zhuang
 */
@Data
public class ListTaskByPageRespAsVo extends PageableVo {

    private List<TaskAsVo> list;
}