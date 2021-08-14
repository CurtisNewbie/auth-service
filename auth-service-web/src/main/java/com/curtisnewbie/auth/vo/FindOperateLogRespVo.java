package com.curtisnewbie.auth.vo;

import com.curtisnewbie.common.vo.PageableVo;
import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import lombok.Data;

import java.util.List;

/**
 * Response vo for finding operate log
 *
 * @author yongjie.zhuang
 */
@Data
public class FindOperateLogRespVo extends PageableVo {

    private List<OperateLogWebVo> operateLogVoList;
}
