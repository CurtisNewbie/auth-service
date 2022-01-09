package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.local.api.LocalAppService;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RequestMapping(value = AppServiceFeign.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AppServiceFeignController implements AppServiceFeign {

    @Autowired
    private LocalAppService localAppService;

    @Override
    public Result<PageablePayloadSingleton<List<AppVo>>> getAllAppInfo(PagingVo pagingVo) {
        return Result.of(localAppService.getAllAppInfo(pagingVo));
    }

    @Override
    public Result<List<AppBriefVo>> getAllAppBriefInfo() {
        return Result.of(localAppService.getAllAppBriefInfo());
    }
}
