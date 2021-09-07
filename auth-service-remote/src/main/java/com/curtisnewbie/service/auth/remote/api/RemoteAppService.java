package com.curtisnewbie.service.auth.remote.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import com.github.pagehelper.PageInfo;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * Remote service for app
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface RemoteAppService {

    /**
     * Get all apps information
     */
    PageInfo<AppVo> getAllAppInfo(@NotNull PagingVo pagingVo);

    /**
     * Get all apps brief info
     */
    List<AppBriefVo> getAllAppBriefInfo();
}
