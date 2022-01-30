package com.curtisnewbie.service.auth.local.vo.cmd;

import lombok.Builder;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * Cmd object
 *
 * @author yongjie.zhuang
 * @see com.curtisnewbie.service.auth.local.api.LocalAccessLogService#moveRecordsToHistory(MoveAccessLogToHistoryCmd)
 */
@Builder
public class MoveAccessLogToHistoryCmd {

    private static final int DEFAULT_BATCH_SIZE = 50;

    /**
     * Records before this date are moved
     */
    private final LocalDateTime before;

    /**
     * Maximum number of records that can be moved
     */
    private final Integer maxCount;

    /**
     * Size of records that are moved in a single batch
     */
    private final Integer batchSize;

    /** Validate object */
    public void validate() {
        Assert.notNull(before, "Must specify 'before' LocalDateTime");
    }

    /**
     * Check whether maxCount is violated or {@code count} is overflowed
     *
     * @param count current count, if count < 0, it's considered an overflow
     */
    public boolean isMaxCountViolated(int count) {
        return count < 0 || count >= maxCount();
    }

    /**
     * Batch size
     */
    public int batchSize() {
        return isBatchSizeLimited() ? batchSize : DEFAULT_BATCH_SIZE;
    }

    /**
     * Max size limit
     */
    public int maxCount() {
        return isMaxCountLimited() ? maxCount : Integer.MAX_VALUE;
    }

    /**
     * Before date
     */
    public LocalDateTime before() {
        return before;
    }

    // --------------------------- private methods ---------------------------

    private boolean isBatchSizeLimited() {
        return batchSize != null;
    }

    private boolean isMaxCountLimited() {
        return maxCount != null;
    }


}
