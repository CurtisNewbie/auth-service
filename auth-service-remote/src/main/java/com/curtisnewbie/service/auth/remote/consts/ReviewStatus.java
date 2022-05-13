package com.curtisnewbie.service.auth.remote.consts;

import lombok.Getter;

/**
 * User Review Status
 *
 * @author yongj.zhuang
 */
@Getter
public enum ReviewStatus {

    /** Pending to be reviewed */
    PENDING(false),

    /** Rejected */
    REJECTED(true),

    /** Approved */
    APPROVED(true);

    private final boolean isDecision;

    ReviewStatus(boolean isDecision) {
        this.isDecision = isDecision;
    }

}
