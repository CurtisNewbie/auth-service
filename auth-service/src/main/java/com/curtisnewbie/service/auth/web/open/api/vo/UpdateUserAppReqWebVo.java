package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.AssertUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Data
public class UpdateUserAppReqWebVo implements Serializable {

    /**
     * User's id
     */
    private Integer userId;

    /**
     * List of app id
     */
    private List<Integer> appIdList;

    public void validate() throws MsgEmbeddedException {
        AssertUtils.notNull(getUserId());
        if (getAppIdList() == null)
            setAppIdList(Collections.emptyList());
    }
}
