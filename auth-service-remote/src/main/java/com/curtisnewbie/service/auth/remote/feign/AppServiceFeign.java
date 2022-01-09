package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@FeignClient(value = FeignConst.SERVICE_NAME, path = AppServiceFeign.PATH)
public interface AppServiceFeign {

    String PATH = "/remote/app";

    /**
     * Get all apps information
     */
    @PostMapping("/list")
    Result<PageablePayloadSingleton<List<AppVo>>> getAllAppInfo(@RequestBody PagingVo pagingVo);

    /**
     * Get all apps brief info
     */
    @PostMapping("/brief/list")
    Result<List<AppBriefVo>> getAllAppBriefInfo();

}
