package com.curtisnewbie.auth.vo;

import lombok.Builder;
import lombok.Data;

/**
 * Events that need to be handled by someone, e.g., administrators
 *
 * @author yongjie.zhuang
 */
@Data
public class HandleEventReqWebVo {

    /** primary key */
    private Integer id;

    /** handling result */
    private Integer result;

    /** extra param */
    private String extra;

    @Builder
    public HandleEventReqWebVo(Integer id, Integer result, String extra) {
        this.id = id;
        this.result = result;
        this.extra = extra;
    }

    public HandleEventReqWebVo() {
    }
}