package com.curtisnewbie.service.auth.remote.feign;

import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
@RequestMapping("/remote/operatelog")
@FeignClient(FeignConst.SERVICE_NAME)
public interface OperateLogServiceFeign {

    /**
     * Save operate_log info
     */
    @PutMapping
    Result<Void> saveOperateLogInfo(@RequestBody OperateLogVo operateLogVo);

    /**
     * Find operate_log info in pages
     */
    @PostMapping("/list")
    Result<PageablePayloadSingleton<List<OperateLogVo>>> findOperateLogInfoInPages(@RequestBody PagingVo pagingVo);
}
