package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.dao.IsDel;
import com.curtisnewbie.common.exceptions.UnrecoverableException;
import com.curtisnewbie.common.util.AssertUtils;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.module.jwt.domain.api.JwtBuilder;
import com.curtisnewbie.module.jwt.domain.api.JwtDecoder;
import com.curtisnewbie.module.jwt.vo.DecodeResult;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.dao.UserKey;
import com.curtisnewbie.service.auth.infrastructure.converters.UserConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserKeyMapper;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserMapper;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.util.PasswordUtil;
import com.curtisnewbie.service.auth.util.RandomNumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.curtisnewbie.common.util.AssertUtils.*;
import static com.curtisnewbie.common.util.PagingUtil.forPage;
import static com.curtisnewbie.service.auth.remote.consts.AuthServiceError.*;
import static com.curtisnewbie.service.auth.util.PasswordUtil.encodePassword;
import static java.lang.String.format;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements LocalUserService {
    private static final String ADMIN_LIMIT_COUNT_KEY = "admin.count.limit";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserKeyMapper userKeyMapper;
    @Autowired
    private LocalEventHandlingService eventHandlingService;
    @Autowired
    private LocalUserAppService userAppService;
    @Autowired
    private Environment environment;
    @Autowired
    private UserConverter cvtr;
    @Autowired
    private JwtBuilder jwtBuilder;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User loadUserByUsername(String s) {
        Objects.requireNonNull(s);
        User userEntity = userMapper.findByUsername(s);
        notNull(userEntity, USER_NOT_FOUND);

        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String findUsernameById(int id) {
        return userMapper.findUsernameById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer findIdByUsername(String username) {
        QueryWrapper<User> condition = new QueryWrapper<>();
        condition.select("id")
                .eq("username", username)
                .last("limit 1");
        User user = userMapper.selectOne(condition);
        if (Objects.isNull(user))
            return null;
        return user.getId();
    }

    @Override
    public void changeRoleAndEnableUser(int userId, UserRole role, String updatedBy) {
        updateUser(UpdateUserVo.builder()
                .id(userId)
                .role(role)
                .isDisabled(UserIsDisabled.NORMAL)
                .updateBy(updatedBy)
                .build());
    }

    @Override
    public void updateUser(UpdateUserVo param) {
        User ue = new User();
        ue.setId(param.getId());
        if (param.getIsDisabled() == null && param.getRole() == null)
            throw new IllegalArgumentException("Nothing to update");
        if (param.getIsDisabled() != null)
            ue.setIsDisabled(param.getIsDisabled());
        if (param.getRole() != null)
            ue.setRole(param.getRole());
        ue.setUpdateBy(param.getUpdateBy());
        ue.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(ue);
    }

    @Override
    public boolean deleteUserLogically(final int userId, String deletedBy) {
        final QueryWrapper<User> where = new QueryWrapper<User>()
                .eq("id", userId)
                .eq("is_del", IsDel.NORMAL);

        final User param = new User();
        param.setIsDel(IsDel.DELETED);
        param.setUpdateBy(deletedBy);

        return userMapper.update(param, where) > 0;
    }

    @Override
    public void updateRole(int id, UserRole role, String updatedBy) {
        updateUser(UpdateUserVo.builder()
                .id(id)
                .role(role)
                .updateBy(updatedBy)
                .build());
    }

    @Override
    public Map<Integer, String> fetchUsernameById(List<Integer> userIds) {
        if (userIds.isEmpty())
            return Collections.emptyMap();

        QueryWrapper<User> cond = new QueryWrapper<>();
        cond.lambda()
                .select(User::getId, User::getUsername)
                .in(User::getId, userIds)
                .eq(User::getIsDel, IsDel.NORMAL);

        List<User> users = userMapper.selectList(cond);
        Map<Integer, String> idToName = new HashMap<>();
        users.forEach(u -> {
            idToName.put(u.getId(), u.getUsername());
        });
        return idToName;
    }

    @Override
    public boolean validateUserPassword(String username, String password) {
        final User ue = validateUserStatusForLogin(username);
        return PasswordUtil.getValidator(ue)
                .givenPassword(password)
                .isMatched();
    }

    @Override
    public String exchangeToken(String username, String password) {
        final UserVo user = login(username, password);
        return buildToken(user);
    }

    @Override
    public String exchangeToken(@NotEmpty String token) {
        DecodeResult decodeResult = jwtDecoder.decode(token);
        AssertUtils.isTrue(decodeResult.isValid(), TOKEN_EXPIRED);

        final String idAsStr = decodeResult.getDecodedJWT().getClaim("id").asString();
        final User ue = userMapper.findById(Long.parseLong(idAsStr));
        return buildToken(cvtr.toVo(ue));
    }

    @Override
    public User getUserInfo(@NotEmpty String token) {
        DecodeResult decodeResult = jwtDecoder.decode(token);
        AssertUtils.isTrue(decodeResult.isValid(), TOKEN_EXPIRED);

        final String idAsStr = decodeResult.getDecodedJWT().getClaim("id").asString();
        User user = userMapper.findById(Long.parseLong(idAsStr));
        AssertUtils.notNull(user, USER_NOT_FOUND);
        AssertUtils.isFalse(user.isDeleted(), USER_NOT_FOUND);
        AssertUtils.equals(user.getIsDisabled(), UserIsDisabled.NORMAL, USER_DISABLED);
        return user;
    }

    @Override
    public UserVo login(String username, String password) {
        final User user = userLogin(username, password);

        log.info("User '{}' login successful, user_info returned", username);
        return cvtr.toVo(user);
    }

    @Override
    public UserVo login(String username, String password, String appName) {
        // validate the credentials first
        final UserVo uv = login(username, password);

        // validate whether current user is allowed to use this application, admin is allowed to use all applications
        isFalse(uv.getRole() != UserRole.ADMIN && !userAppService.isUserAllowedToUseApp(uv.getId(), appName), USER_NOT_PERMITTED);
        return uv;
    }

    @Override
    public void register(RegisterUserVo registerUserVo) {
        notNull(registerUserVo.getUsername());
        notNull(registerUserVo.getPassword());
        notNull(registerUserVo.getRole());
        isNull(userMapper.findIdByUsername(registerUserVo.getUsername()), USER_ALREADY_REGISTERED);

        // limit the total number of administrators
        if (registerUserVo.getRole() == UserRole.ADMIN) {
            checkAdminQuota();
        }

        User userEntity = toUserEntity(registerUserVo);
        userEntity.setIsDisabled(UserIsDisabled.NORMAL);

        log.info("New user '{}' successfully registered, role: {}", registerUserVo.getUsername(), registerUserVo.getRole().getValue());
        userMapper.insert(userEntity);
    }

    @Override
    public void requestRegistrationApproval(RegisterUserVo registerUserVo) {
        notNull(registerUserVo.getUsername());
        notNull(registerUserVo.getPassword());
        notNull(registerUserVo.getRole());
        isNull(userMapper.findIdByUsername(registerUserVo.getUsername()), USER_ALREADY_REGISTERED);

        // limit the total number of administrators
        if (registerUserVo.getRole() == UserRole.ADMIN) {
            checkAdminQuota();
        }

        // set user disabled, this will be handled by the admin
        User user = toUserEntity(registerUserVo);
        user.setIsDisabled(UserIsDisabled.DISABLED);
        userMapper.insert(user);

        log.info("New user '{}' successfully registered, role: {}, currently disabled and waiting for approval",
                registerUserVo.getUsername(), registerUserVo.getRole().getValue());

        // generate a handling_event for registration request
        eventHandlingService.createEvent(
                CreateEventHandlingCmd.builder()
                        .body(String.valueOf(user.getId()))
                        .type(EventHandlingType.REGISTRATION_EVENT)
                        .description(format("User '%s' requests registration approval", user.getUsername()))
                        .build()
        );
        log.info("Created event_handling for {}'s registration", registerUserVo.getUsername());
    }

    @Override
    public void updatePassword(final String newPassword, final String oldPassword, long id) {
        final User ue = userMapper.findById(id);
        notNull(ue, USER_NOT_FOUND);

        final boolean isPasswordMatched = PasswordUtil.getValidator(ue)
                .givenPassword(oldPassword)
                .isMatched();
        isTrue(isPasswordMatched, PASSWORD_INCORRECT);

        final boolean isUpdated = userMapper.updatePwd(encodePassword(newPassword, ue.getSalt()), id) > 0;
        if (isUpdated)
            log.info("User_id '{}' successfully changed password.", id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageablePayloadSingleton<List<UserInfoVo>> findUserInfoByPage(FindUserInfoVo vo) {
        User ue = new User();
        if (vo.getIsDisabled() != null)
            ue.setIsDisabled(vo.getIsDisabled());
        if (vo.getRole() != null)
            ue.setRole(vo.getRole());
        ue.setUsername(vo.getUsername());

        IPage<User> pge = userMapper.findUserInfoBy(forPage(vo.getPagingVo()), ue);
        return PagingUtil.toPageList(pge, cvtr::toInfoVo);
    }

    @Override
    public void disableUserById(int id, String disabledBy) {
        updateUser(UpdateUserVo.builder()
                .id(id)
                .isDisabled(UserIsDisabled.DISABLED)
                .updateBy(disabledBy)
                .build());
    }

    @Override
    public void enableUserById(int id, String enabledBy) {
        updateUser(UpdateUserVo.builder()
                .id(id)
                .isDisabled(UserIsDisabled.NORMAL)
                .updateBy(enabledBy)
                .build());
    }

    // ---------------------- private helper methods -----------------

    private String buildToken(UserVo user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("id", user.getId().toString());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole().getValue());

        final String appNames = userAppService.getAppsPermittedForUser(user.getId())
                .stream()
                .map(AppBriefVo::getName)
                .collect(Collectors.joining(","));
        claims.put("appNames", appNames);

        // by default valid for 15 minutes
        return jwtBuilder.encode(claims, LocalDateTime.now().plusMinutes(15));
    }

    private User userLogin(String username, String password) {
        final User ue = validateUserStatusForLogin(username);

        // validate the password first
        final boolean isPwdCorrect = PasswordUtil.getValidator(ue)
                .givenPassword(password)
                .isMatched();

        // if the password is incorrect, may it's a token, validate it
        if (!isPwdCorrect) {
            final QueryWrapper<UserKey> cond = new QueryWrapper<UserKey>()
                    .eq("user_id", ue.getId())
                    .eq("secret_key", password)
                    .ge("expiration_time", LocalDateTime.now())
                    .eq("is_del", IsDel.NORMAL)
                    .last("limit 1");

            // it's not a token, or it's just incorrect as well
            notNull(userKeyMapper.selectOne(cond), PASSWORD_INCORRECT);
        }
        return ue;
    }

    private User toUserEntity(RegisterUserVo registerUserVo) {
        User u = new User();
        u.setUsername(registerUserVo.getUsername());
        u.setRole(registerUserVo.getRole());
        u.setSalt(RandomNumUtil.randomNoStr(5));
        u.setPassword(encodePassword(registerUserVo.getPassword(), u.getSalt()));
        u.setCreateBy(registerUserVo.getCreateBy());
        u.setCreateTime(LocalDateTime.now());
        return u;
    }

    private static Optional<Integer> parseInteger(String value) {
        if (value == null)
            return Optional.empty();
        try {
            int count = Integer.parseInt(value);
            if (count < 0)
                return Optional.empty();
            return Optional.of(count);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void checkAdminQuota() {
        // limit the total number of administrators
        Optional<Integer> optInt = parseInteger(environment.getProperty(ADMIN_LIMIT_COUNT_KEY));
        if (optInt.isPresent()) {
            int currCntOfAdmin = userMapper.countAdmin();
            // exceeded the max num of administrators
            if (currCntOfAdmin >= optInt.get()) {
                log.info("Try to register user as admin, but the maximum number of admin ({}) is exceeded.", optInt.get());
                throw new UnrecoverableException(ADMIN_REG_NOT_ALLOWED);
            }
        }
    }

    private User validateUserStatusForLogin(String username) {
        final User ue = loadUserByUsername(username);
        isTrue(ue.getIsDisabled() == UserIsDisabled.NORMAL, USER_DISABLED);
        return ue;
    }

}
