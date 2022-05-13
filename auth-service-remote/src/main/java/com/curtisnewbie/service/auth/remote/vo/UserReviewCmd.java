package com.curtisnewbie.service.auth.remote.vo;

import com.curtisnewbie.service.auth.remote.consts.ReviewStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yongj.zhuang
 */
@Data
public class UserReviewCmd {

    @NotNull
    private Integer userId;

    @NotNull
    private ReviewStatus reviewStatus;
}
