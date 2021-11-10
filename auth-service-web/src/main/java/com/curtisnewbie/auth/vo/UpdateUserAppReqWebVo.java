package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.ValidUtils;
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
        ValidUtils.requireNonNull(getUserId());
        if (getAppIdList() == null)
            setAppIdList(Collections.emptyList());
    }
}
