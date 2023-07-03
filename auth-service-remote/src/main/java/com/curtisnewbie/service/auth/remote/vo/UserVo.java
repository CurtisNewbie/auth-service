package com.curtisnewbie.service.auth.remote.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * User vo
 *
 * @author yongjie.zhuang
 */
@Data
public class UserVo {

    /** primary key */
    private Integer id;

    /** userNo (unique) */
    private String userNo;

    /** username (must be unique) */
    private String username;

    /** when the user is created */
    private LocalDateTime createTime;

    /** when the user is updated */
    private LocalDateTime updateTime;

    /** who updated this user */
    private String updateBy;

    /** roleNo */
    private String roleNo;

    /** who created this user */
    private String createBy;
}
