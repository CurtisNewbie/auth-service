package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.common.vo.PageableVo;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
public class FindEventHandlingByPageReqVo extends PageableVo {

    /** type of event, 1-registration */
    private Integer type;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private Integer status;

    @Builder
    public FindEventHandlingByPageReqVo(Integer type, Integer status) {
        this.type = type;
        this.status = status;
    }

    public FindEventHandlingByPageReqVo() {
    }
}