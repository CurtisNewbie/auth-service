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

    @Builder
    public HandleEventReqWebVo(Integer id, Integer result) {
        this.id = id;
        this.result = result;
    }

    public HandleEventReqWebVo() {
    }
}