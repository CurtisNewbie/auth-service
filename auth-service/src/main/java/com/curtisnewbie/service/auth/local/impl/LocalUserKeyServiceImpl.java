package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.service.auth.dao.UserKey;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserKeyMapper;
import com.curtisnewbie.service.auth.local.api.LocalUserKeyService;
import com.curtisnewbie.service.auth.local.vo.cmd.GenerateUserKeyCmd;
import com.curtisnewbie.service.auth.local.vo.resp.GenerateUserKeyResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
public class LocalUserKeyServiceImpl implements LocalUserKeyService {

    @Autowired
    private UserKeyMapper userKeyMapper;

    @Override
    public GenerateUserKeyResp generateUserKey(GenerateUserKeyCmd cmd) {
        Assert.isTrue(cmd.getExpirationTime().isAfter(LocalDateTime.now()), "expirationTime must be after current time");
        final String key = cmd.getUserId() + UUID.randomUUID().toString();

        final UserKey insertParam = new UserKey();
        insertParam.setSecretKey(key);
        insertParam.setExpirationTime(cmd.getExpirationTime());
        insertParam.setUserId(cmd.getUserId());
        insertParam.setCreateBy(cmd.getCreateBy());
        userKeyMapper.insert(insertParam);

        return GenerateUserKeyResp.builder()
                .expirationTime(cmd.getExpirationTime())
                .userId(cmd.getUserId())
                .key(key)
                .build();
    }
}
