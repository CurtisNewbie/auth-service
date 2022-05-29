package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * Service for app
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface LocalAppService {

    /**
     * Get all apps information
     */
    PageableList<AppVo> getAllAppInfo(@NotNull PagingVo pagingVo);

    /**
     * Get all apps brief info
     */
    List<AppBriefVo> getAllAppBriefInfo();

    /**
     * Get app_id by name
     */
    Integer getAppIdByName(String appName);

}
