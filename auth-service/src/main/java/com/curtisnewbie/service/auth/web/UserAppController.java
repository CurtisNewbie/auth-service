package com.curtisnewbie.service.auth.web;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.infrastructure.converters.AppWebConverter;
import com.curtisnewbie.service.auth.remote.api.RemoteAppService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import com.curtisnewbie.service.auth.vo.AppWebVo;
import com.curtisnewbie.service.auth.vo.GetAppsForUserReqVo;
import com.curtisnewbie.service.auth.vo.UpdateUserAppReqWebVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.curtisnewbie.common.util.BeanCopyUtils.mapTo;
import static com.curtisnewbie.common.util.BeanCopyUtils.toType;

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

    @Autowired
    private AppWebConverter cvtr;

    @PostMapping("/list/all")
    @PreAuthorize("hasAuthority('admin')")
    public Result<PageablePayloadSingleton<List<AppWebVo>>> listApps(@RequestBody PageablePayloadSingleton<AppWebVo> req)
            throws MsgEmbeddedException {
        ValidUtils.requireNonNull(req.getPagingVo());

        PageablePayloadSingleton<List<AppVo>> pps = remoteAppService.getAllAppInfo(req.getPagingVo());
        PageablePayloadSingleton<List<AppWebVo>> resp = new PageablePayloadSingleton<>();
        resp.setPagingVo(pps.getPagingVo());
        resp.setPayload(mapTo(pps.getPayload(), cvtr::toWebVo));
        return Result.of(resp);
    }

    @GetMapping("/list/brief/all")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<AppBriefVo>> listAppsBriefInfo() {
        return Result.of(remoteAppService.getAllAppBriefInfo());
    }

    @PostMapping("/list/user")
    @PreAuthorize("hasAuthority('admin')")
    public Result<List<AppBriefVo>> getAppsForUser(@RequestBody GetAppsForUserReqVo vo) throws MsgEmbeddedException {

        vo.validate();
        return Result.of(remoteUserAppService.getAppsPermittedForUser(vo.getUserId()));
    }

    @PostMapping("/user/update")
    @PreAuthorize("hasAuthority('admin')")
    public Result<Void> updateUserApps(@RequestBody UpdateUserAppReqWebVo reqVo) throws MsgEmbeddedException {
        reqVo.validate();
        remoteUserAppService.updateUserApp(toType(reqVo, UpdateUserAppReqCmd.class));
        return Result.ok();
    }

}
