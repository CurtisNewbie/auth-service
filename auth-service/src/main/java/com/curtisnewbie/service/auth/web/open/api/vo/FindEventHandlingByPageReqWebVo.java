package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindEventHandlingByPageReqWebVo extends PageableVo {

    /** type of event, 1-registration */
    private EventHandlingType type;

    /** status of event, 0-no need to handle, 1-to be handled, 2-handled */
    private EventHandlingStatus status;

    /** handle result, 1-accept, 2-reject */
    private EventHandlingResult handleResult;
}