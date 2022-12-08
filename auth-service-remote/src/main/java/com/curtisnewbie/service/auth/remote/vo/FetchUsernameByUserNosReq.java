package com.curtisnewbie.service.auth.remote.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FetchUsernameByUserNosReq {

    @NotNull
    List<String> userNos;
}
