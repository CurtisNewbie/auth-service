package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.service.auth.remote.vo.EventHandlingVo;
import lombok.Data;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Data
public class FindEventHandlingByPageRespWebVo extends PageableVo {

    private List<EventHandlingWebVo> list;
}
