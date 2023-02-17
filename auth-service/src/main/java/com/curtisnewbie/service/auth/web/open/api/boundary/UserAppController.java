package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.advice.RoleControlled;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalAppService;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import com.curtisnewbie.service.auth.remote.vo.UpdateUserAppReqCmd;
import com.curtisnewbie.service.auth.web.open.api.vo.AppWebVo;
import com.curtisnewbie.service.auth.web.open.api.vo.GetAppsForUserReqVo;
import com.curtisnewbie.service.auth.web.open.api.vo.UpdateUserAppReqWebVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.curtisnewbie.common.util.BeanCopyUtils.*;

/**
 * @author yongjie.zhuang
 */
@Validated
@RoleControlled(rolesRequired = "admin")
@RequestMapping("${web.base-path}/app")
@RestController
public class UserAppController {

    @Autowired
    private LocalAppService appService;

    @Autowired
    private LocalUserAppService userAppService;

    @PostMapping("/list/all")
    public Result<PageableList<AppWebVo>> listApps(@RequestBody PageableVo<AppWebVo> req)
            throws MsgEmbeddedException {

        PageableList<AppVo> pps = appService.getAllAppInfo(req.getPagingVo());
        PageableList<AppWebVo> resp = new PageableList<>();
        resp.setPagingVo(pps.getPagingVo());
        resp.setPayload(toTypeList(pps.getPayload(), AppWebVo.class));
        return Result.of(resp);
    }

    @GetMapping("/list/brief/all")
    public Result<List<AppBriefVo>> listAppsBriefInfo() {
        return Result.of(appService.getAllAppBriefInfo());
    }

    @PostMapping("/list/user")
    public Result<List<AppBriefVo>> getAppsForUser(@Valid @RequestBody GetAppsForUserReqVo vo) throws MsgEmbeddedException {
        return Result.of(userAppService.getAppsPermittedForUser(vo.getUserId()));
    }

    @PostMapping("/user/update")
    public Result<Void> updateUserApps(@RequestBody UpdateUserAppReqWebVo reqVo) throws MsgEmbeddedException {
        reqVo.validate();
        userAppService.updateUserApp(toType(reqVo, UpdateUserAppReqCmd.class));
        return Result.ok();
    }

}
