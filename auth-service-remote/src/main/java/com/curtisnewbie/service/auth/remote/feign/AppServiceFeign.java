package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.AppBriefVo;
import com.curtisnewbie.service.auth.remote.vo.AppVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RequestMapping("/remote/app")
@FeignClient(FeignConst.SERVICE_NAME)
public interface AppServiceFeign {

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
