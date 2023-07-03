package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.vo.PageableVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Response vo for finding operate log
 *
 * @author yongjie.zhuang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FindOperateLogRespWebVo extends PageableVo<Void> {

    private List<OperateLogWebVo> operateLogVoList;
}
