package com.curtisnewbie.service.auth.web.open.api.vo;

import com.curtisnewbie.common.vo.PageableVo;
import lombok.Data;

import java.util.List;

/**
 * Response vo for finding operate log
 *
 * @author yongjie.zhuang
 */
@Data
public class FindOperateLogRespWebVo extends PageableVo {

    private List<OperateLogWebVo> operateLogVoList;
}
