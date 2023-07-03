package com.curtisnewbie.service.auth.web.open.api.vo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author yongjie.zhuang
 */
@Data
@ToString(exclude = "secretKey")
public class UserKeyVo {

    private Integer id;

    /** secret key */
    private String secretKey;

    /** name of the key */
    private String name;

    /** when the key is expired */
    private LocalDateTime expirationTime;

    /** when the record is created */
    private LocalDateTime createTime;

}
