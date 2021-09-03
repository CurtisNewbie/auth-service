package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * UserInfo web vo
 *
 * @author yongjie.zhuang
 */
@Data
public class UserInfoWebVo {

    /** id */
    private Integer id;

    /**
     * username
     */
    private String username;

    /**
     * role
     */
    private String role;

    /** whether the user is disabled, 0-normal, 1-disabled */
    private Integer isDisabled;

    /** when the user is created */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private Date createTime;

    /** when the user is updated */
    @JsonFormat(pattern = DateUtils.DD_MM_YYYY_HH_MM)
    private Date updateTime;

    /** who updated this user */
    private String updateBy;

    /** who created this user */
    private String createBy;

}
