package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
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
    PageablePayloadSingleton<List<AppVo>> getAllAppInfo(@NotNull PagingVo pagingVo);

    /**
     * Get all apps brief info
     */
    List<AppBriefVo> getAllAppBriefInfo();

}
