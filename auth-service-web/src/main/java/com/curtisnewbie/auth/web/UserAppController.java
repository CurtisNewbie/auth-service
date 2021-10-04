package com.curtisnewbie.auth.web;

import com.curtisnewbie.auth.vo.AppWebVo;
import com.curtisnewbie.auth.vo.GetAppsForUserReqVo;
import com.curtisnewbie.auth.vo.UpdateUserAppReqWebVo;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.api.RemoteAppService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import com.curtisnewbie.service.auth.remote.vo.GetAppsPermittedForUserReqVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.curtisnewbie.common.util.BeanCopyUtils.toType;
import static com.curtisnewbie.common.util.BeanCopyUtils.toTypeList;

/**
 * @author yongjie.zhuang
 */
@RequestMapping("${web.base-path}/app")
@RestController
public class UserAppController {

    @DubboReference
    private RemoteAppService remoteAppService;

    @DubboReference
    private RemoteUserAppService remoteUserAppService;

    @PostMapping("/list/all")
    @PreAuthorize("hasAuthority('admin')")
    public Result<PageablePayloadSingleton<List<AppWebVo>>> listApps(@RequestBody PageablePayloadSingleton<AppWebVo> req)
            throws MsgEmbeddedException {
        ValidUtils.requireNonNull(req.getPagingVo());

        PageInfo<AppVo> pi = remoteAppService.getAllAppInfo(req.getPagingVo());
        PageablePayloadSingleton<List<AppWebVo>> resp = new PageablePayloadSingleton<>();
        resp.setPagingVo(new PagingVo().ofPageInfoTotal(pi));
        resp.setPayload(toTypeList(pi.getList(), AppWebVo.class));
        return Result.of(resp);
    }

    @GetMapping("/list/brief/all")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<AppBriefVo>> listAppsBriefInfo()
            throws MsgEmbeddedException {
        return Result.of(remoteAppService.getAllAppBriefInfo());
    }

    @PostMapping("/list/user")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<AppBriefVo>> getAppsForUser(@RequestBody GetAppsForUserReqVo vo)
            throws MsgEmbeddedException {
        ValidUtils.requireNonNull(vo.getUserId());

        return Result.of(remoteUserAppService.getAppsPermittedForUser(vo.getUserId()));
    }

    @PostMapping("/user/update")
    @PreAuthorize("hasAuthority('admin')")
    public Result<Void> updateUserApps(@RequestBody UpdateUserAppReqWebVo reqVo) throws MsgEmbeddedException {
        ValidUtils.requireNonNull(reqVo.getUserId());
        if (reqVo.getAppIdList() == null)
            reqVo.setAppIdList(Collections.emptyList());

        remoteUserAppService.updateUserApp(toType(reqVo, UpdateUserAppReqVo.class));
        return Result.ok();
    }

}
