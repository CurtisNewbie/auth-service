package com.curtisnewbie.auth.web;

import com.curtisnewbie.auth.vo.ListTaskByPageReqAsVo;
import com.curtisnewbie.auth.vo.ListTaskByPageRespAsVo;
import com.curtisnewbie.auth.vo.TaskAsVo;
import com.curtisnewbie.auth.vo.TriggerTaskReqAsVo;
import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.module.task.constants.TaskConcurrentEnabled;
import com.curtisnewbie.module.task.constants.TaskEnabled;
import com.curtisnewbie.module.task.scheduling.JobUtils;
import com.curtisnewbie.module.task.service.NodeCoordinationService;
import com.curtisnewbie.module.task.service.TaskService;
import com.curtisnewbie.module.task.vo.ListTaskByPageReqVo;
import com.curtisnewbie.module.task.vo.ListTaskByPageRespVo;
import com.curtisnewbie.module.task.vo.TaskVo;
import com.curtisnewbie.module.task.vo.UpdateTaskReqVo;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private NodeCoordinationService nodeCoordinationService;

    @LogOperation(name = "/task/list", description = "list tasks")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/list")
    public Result<ListTaskByPageRespAsVo> listTaskByPage(@RequestBody ListTaskByPageReqAsVo reqVo) throws MsgEmbeddedException {
        ValidUtils.requireNonNull(reqVo.getPagingVo());
        PageInfo<ListTaskByPageRespVo> pi = taskService.listByPage(BeanCopyUtils.toType(reqVo, ListTaskByPageReqVo.class),
                reqVo.getPagingVo());
        ListTaskByPageRespAsVo resp = new ListTaskByPageRespAsVo();
        resp.setPagingVo(new PagingVo().ofTotal(pi.getTotal()));
        resp.setList(BeanCopyUtils.toTypeList(pi.getList(), TaskAsVo.class));
        return Result.of(resp);
    }

    @LogOperation(name = "/task/update", description = "update task")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody UpdateTaskReqVo vo) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNonNull(vo.getId());

        if (vo.getCronExpr() != null && !JobUtils.isCronExprValid(vo.getCronExpr())) {
            return Result.error("Cron expression illegal");
        }
        if (vo.getEnabled() != null) {
            TaskEnabled tce = EnumUtils.parse(vo.getEnabled(), TaskEnabled.class);
            ValidUtils.requireNonNull(tce);
        }
        if (vo.getConcurrentEnabled() != null) {
            TaskConcurrentEnabled tce = EnumUtils.parse(vo.getConcurrentEnabled(), TaskConcurrentEnabled.class);
            ValidUtils.requireNonNull(tce);
        }
        vo.setUpdateBy(AuthUtil.getUsername());
        taskService.updateById(vo);
        return Result.ok();
    }

    @LogOperation(name = "/task/trigger", description = "trigger task")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/trigger")
    public Result<Void> trigger(@RequestBody TriggerTaskReqAsVo vo) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNonNull(vo.getId());
        TaskVo tv = taskService.selectById(vo.getId());
        nodeCoordinationService.coordinateJobTriggering(tv, AuthUtil.getUsername());
        return Result.ok();
    }
}
