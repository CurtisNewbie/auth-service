package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.advice.RoleControlled;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.goauth.client.PathDoc;
import com.curtisnewbie.service.auth.local.api.LocalOperateLogService;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/operate")
public class OperateLogController {

    @Autowired
    private LocalOperateLogService operateLogService;

    @PathDoc(description = "List operate logs")
    @RoleControlled(rolesRequired = "admin")
    @PostMapping("/history")
    public Result<FindOperateLogRespWebVo> findByPage(@RequestBody PagingVo pagingVo) throws MsgEmbeddedException {
        PageableList<OperateLogVo> pv = operateLogService.findOperateLogInfoInPages(pagingVo);
        FindOperateLogRespWebVo res = new FindOperateLogRespWebVo();
        res.setPagingVo(pv.getPagingVo());
        res.setOperateLogVoList(BeanCopyUtils.toTypeList(pv.getPayload(), OperateLogWebVo.class));
        return Result.of(res);
    }

}
