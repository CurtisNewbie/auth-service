package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.service.auth.remote.consts.EventHandlingResult;
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
public class HandleEventReqWebVo {

    /** primary key */
    private Integer id;

    /** handling result */
    private EventHandlingResult result;

    /** extra param */
    private String extra;


}