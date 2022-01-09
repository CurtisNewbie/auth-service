package com.curtisnewbie.service.auth.remote.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DisableUserByIdCmd {

    /** user id */
    private int id;

    private String disabledBy = "";
}
