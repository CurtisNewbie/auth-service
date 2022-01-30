package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.vo.PageableVo;
import lombok.Data;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Data
public class FindEventHandlingByPageRespWebVo extends PageableVo {

    private List<EventHandlingWebVo> list;
}
