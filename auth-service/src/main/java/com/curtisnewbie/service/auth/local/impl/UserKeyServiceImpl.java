package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curtisnewbie.common.dao.IsDel;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.util.RandomUtils;
import com.curtisnewbie.common.vo.PageableList;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.service.auth.dao.UserKey;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserKeyMapper;
import com.curtisnewbie.service.auth.local.api.UserKeyService;
import com.curtisnewbie.service.auth.local.vo.cmd.GenerateUserKeyCmd;
import com.curtisnewbie.service.auth.local.vo.resp.GenerateUserKeyResp;
import com.curtisnewbie.service.auth.web.open.api.vo.UserKeyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

import static com.curtisnewbie.common.util.PagingUtil.toPageableList;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author yongjie.zhuang
 */
@Service
public class UserKeyServiceImpl implements UserKeyService {

    public static final int KEY_LEN = 64;

    @Autowired
    private UserKeyMapper userKeyMapper;

    @Override
    public GenerateUserKeyResp generateUserKey(GenerateUserKeyCmd cmd) {
        Assert.isTrue(cmd.getExpirationTime().isAfter(LocalDateTime.now()),
                "expirationTime must be after current time");
        final String key = RandomUtils.randomAlphaNumeric(KEY_LEN);

        final UserKey insertParam = new UserKey();
        insertParam.setName(cmd.getName());
        insertParam.setSecretKey(key);
        insertParam.setExpirationTime(cmd.getExpirationTime());
        insertParam.setUserId(cmd.getUserId());
        userKeyMapper.insert(insertParam);

        return GenerateUserKeyResp.builder()
                .expirationTime(cmd.getExpirationTime())
                .userId(cmd.getUserId())
                .key(key)
                .build();
    }

    @Override
    public PageableList<UserKeyVo> listUserKeys(int userId, String name, PagingVo p) {
        final Page<UserKey> ukPage = userKeyMapper.selectPage(PagingUtil.forPage(p),
                new LambdaQueryWrapper<UserKey>()
                        .select(UserKey::getId, UserKey::getSecretKey, UserKey::getExpirationTime,
                                UserKey::getCreateTime,
                                UserKey::getName)
                        .like(hasText(name), UserKey::getName, name)
                        .eq(UserKey::getUserId, userId)
                        .eq(UserKey::getIsDel, IsDel.NORMAL)
                        .orderByDesc(UserKey::getId));
        return toPageableList(ukPage, uk -> BeanCopyUtils.toType(uk, UserKeyVo.class));
    }

    @Override
    public void deleteUserKey(int userId, int keyId) {
        userKeyMapper.update(null, new LambdaUpdateWrapper<UserKey>()
                .set(UserKey::getIsDel, IsDel.DELETED)
                .eq(UserKey::getUserId, userId)
                .eq(UserKey::getId, keyId)
                .eq(UserKey::getIsDel, IsDel.NORMAL));
    }

    @Override
    public boolean isUserKeyValid(int userId, String key) {
        if (key == null)
            return false;

        final LambdaQueryWrapper<UserKey> cond = new LambdaQueryWrapper<UserKey>()
                .eq(UserKey::getUserId, userId)
                .eq(UserKey::getSecretKey, key)
                .gt(UserKey::getExpirationTime, LocalDateTime.now())
                .eq(UserKey::getIsDel, IsDel.NORMAL)
                .last("limit 1");

        return userKeyMapper.selectOne(cond) != null;
    }
}
