package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@Validated
@FeignClient(FeignConst.SERVICE_NAME)
@RequestMapping("/remote/accesslog/")
public interface AccessLogServiceFeign {

    /**
     * Save access_log entity
     */
    @PutMapping
    Result<Void> save(@RequestBody AccessLogInfoVo accessLogVo);

    /**
     * Find access log info in pages
     *
     * @param paging pagination param
     */
    @NotNull
    @PostMapping("/list")
    Result<PageablePayloadSingleton<List<AccessLogInfoVo>>> findAccessLogInfoByPage(@RequestBody PagingVo paging);
}
