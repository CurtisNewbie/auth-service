package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.vo.PageableVo;
import lombok.Builder;
import lombok.Data;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
public class FindEventHandlingByPageReqWebVo extends PageableVo {

    /** type of event, 1-registration */
    private Integer type;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private Integer status;

    /** handle result, 1-accept, 2-reject */
    private Integer handleResult;

    @Builder
    public FindEventHandlingByPageReqWebVo(Integer type, Integer status, Integer handleResult) {
        this.type = type;
        this.status = status;
        this.handleResult = handleResult;
    }

    public FindEventHandlingByPageReqWebVo() {
    }
}