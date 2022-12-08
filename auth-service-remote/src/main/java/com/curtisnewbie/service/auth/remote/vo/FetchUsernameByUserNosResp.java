package com.curtisnewbie.service.auth.remote.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author yongjie.zhuang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchUsernameByUserNosResp {

    /** userNos -> username map */
    private Map<String, String> userNoToUsername;

}
