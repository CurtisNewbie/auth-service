package com.curtisnewbie.service.auth.web.open.api.boundary;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.module.task.constants.TaskConcurrentEnabled;
import com.curtisnewbie.module.task.constants.TaskEnabled;
import com.curtisnewbie.module.task.scheduling.JobUtils;
import com.curtisnewbie.module.task.service.NodeCoordinationService;
import com.curtisnewbie.module.task.service.TaskHistoryService;
import com.curtisnewbie.module.task.service.TaskService;
import com.curtisnewbie.module.task.vo.ListTaskByPageRespVo;
import com.curtisnewbie.module.task.vo.ListTaskHistoryByPageRespVo;
import com.curtisnewbie.module.task.vo.TaskVo;
import com.curtisnewbie.module.task.vo.UpdateTaskReqVo;
import com.curtisnewbie.service.auth.infrastructure.converters.TaskAsConverter;
import com.curtisnewbie.service.auth.infrastructure.converters.TaskHistoryAsConverter;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.web.open.api.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.curtisnewbie.common.util.BeanCopyUtils.mapTo;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("${web.base-path}/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskHistoryService taskHistoryService;

    @Autowired
    private NodeCoordinationService nodeCoordinationService;

    @Autowired
    private TaskAsConverter taskAsConverter;

    @Autowired
    private TaskHistoryAsConverter taskHistoryAsConverter;

    @LogOperation(name = "/task/list", description = "list tasks")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/list")
    public Result<ListTaskByPageRespAsVo> listTaskByPage(@RequestBody ListTaskByPageReqAsVo reqVo) throws MsgEmbeddedException {
        ValidUtils.requireNonNull(reqVo.getPagingVo());

        PageablePayloadSingleton<List<ListTaskByPageRespVo>> pi = taskService.listByPage(taskAsConverter.toListTaskByPageReqAsVo(reqVo),
                reqVo.getPagingVo());
        ListTaskByPageRespAsVo resp = new ListTaskByPageRespAsVo();
        resp.setPagingVo(pi.getPagingVo());
        resp.setList(
                pi.getPayload()
                        .stream()
                        .map(taskAsConverter::toTaskAsVo)
                        .collect(Collectors.toList())
        );
        return Result.of(resp);
    }

    @LogOperation(name = "/task/history", description = "list task history")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/history")
    public Result<ListTaskHistoryByPageRespWebVo> listTaskHistoryByPage(@RequestBody ListTaskHistoryByPageReqWebVo reqVo)
            throws MsgEmbeddedException {
        reqVo.validate();

        PageablePayloadSingleton<List<ListTaskHistoryByPageRespVo>> pi = taskHistoryService.findByPage(taskHistoryAsConverter.toListTaskHistoryByPageReqVo(reqVo));
        ListTaskHistoryByPageRespWebVo resp = new ListTaskHistoryByPageRespWebVo();
        resp.setList(mapTo(pi.getPayload(), taskHistoryAsConverter::toTaskHistoryWebVo));
        resp.setPagingVo(pi.getPagingVo());
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
