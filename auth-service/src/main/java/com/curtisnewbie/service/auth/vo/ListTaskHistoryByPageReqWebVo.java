package com.curtisnewbie.service.auth.vo;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageableVo;
import lombok.Data;

import java.util.Date;

/**
 * @author yongjie.zhuang
 */
@Data
public class ListTaskHistoryByPageReqWebVo extends PageableVo {

    /** Task id */
    private Integer taskId;

    /** task's name */
    private String jobName;

    /** start time */
    private Date startTime;

    /** end time */
    private Date endTime;

    /** task triggered by */
    private String runBy;

    public void validate() throws MsgEmbeddedException {
        ValidUtils.requireNonNull(getPagingVo());
    }
}
