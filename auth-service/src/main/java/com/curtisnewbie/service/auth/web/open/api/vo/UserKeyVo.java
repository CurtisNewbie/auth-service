package com.curtisnewbie.service.auth.web.open.api.vo;


import lombok.*;

import java.time.*;

/**
 * @author yongjie.zhuang
 */
@Data
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

    @ToString.Include(name = "secretKey")
    public String secretKeyMask() {
        return secretKey != null ? "****" : null;
    }

}
