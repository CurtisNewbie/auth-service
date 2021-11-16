package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.common.util.PagingUtil;
import com.curtisnewbie.common.vo.PageablePayloadSingleton;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.infrastructure.converters.UserConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserMapper;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
import com.curtisnewbie.service.auth.local.api.LocalUserAppService;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingStatus;
import com.curtisnewbie.service.auth.remote.consts.EventHandlingType;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.util.PasswordUtil;
import com.curtisnewbie.service.auth.util.RandomNumUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.curtisnewbie.common.util.PagingUtil.forPage;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
@DubboService(interfaceClass = RemoteUserService.class)
public class UserServiceImpl implements LocalUserService {
    private static final String ADMIN_LIMIT_COUNT_KEY = "admin.count.limit";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LocalEventHandlingService eventHandlingService;
    @Autowired
    private LocalUserAppService userAppService;
    @Autowired
    private Environment environment;
    @Autowired
    private UserConverter cvtr;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User loadUserByUsername(@NotEmpty String s) throws UsernameNotFoundException {
        Objects.requireNonNull(s);
        User userEntity = userMapper.findByUsername(s);
        if (userEntity == null)
            throw new UsernameNotFoundException("Username '" + s + "' not found");
        return userEntity;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String findUsernameById(int id) {
        return userMapper.findUsernameById(id);
    }

    @Override
    public Integer findIdByUsername(@NotEmpty String username) {
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
    public void changeRoleAndEnableUser(int userId, @NotNull UserRole role, @Nullable String updatedBy) {
        updateUser(UpdateUserVo.builder()
                .id(userId)
                .role(role)
                .isDisabled(UserIsDisabled.NORMAL)
                .updateBy(updatedBy)
                .build());
    }

    @Override
    public void updateUser(@NotNull UpdateUserVo param) {
        User ue = new User();
        ue.setId(param.getId());
        if (param.getIsDisabled() == null && param.getRole() == null)
            throw new IllegalArgumentException("Nothing to update");
        if (param.getIsDisabled() != null)
            ue.setIsDisabled(param.getIsDisabled().getValue());
        if (param.getRole() != null)
            ue.setRole(param.getRole().getValue());
        ue.setUpdateBy(param.getUpdateBy());
        ue.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(ue);
    }

    @Override
    public void deleteUser(final int userId, @NotEmpty String deletedBy) {
        int c = userMapper.moveDisabledUser(userId, deletedBy);
        if (c > 0) {
            userMapper.deleteUser(userId);
        }
    }

    @Override
    public void updateRole(int id, @NotNull UserRole role, @Nullable String updatedBy) {
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
                .in(User::getId, userIds);

        List<User> users = userMapper.selectList(cond);
        Map<Integer, String> idToName = new HashMap<>();
        users.forEach(u -> {
            idToName.put(u.getId(), u.getUsername());
        });
        return idToName;
    }

    @Override
    public UserVo login(@NotEmpty String username, @NotEmpty String password) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException {

        User ue = loadUserByUsername(username);
        if (ue == null) {
            log.info("User '{}' attempt to login, but username is not found.", username);
            throw new UsernameNotFoundException(username);
        }
        UserIsDisabled isDisabled = EnumUtils.parse(ue.getIsDisabled(), UserIsDisabled.class);
        Objects.requireNonNull(isDisabled, "Illegal is_disabled value");
        if (isDisabled == UserIsDisabled.DISABLED) {
            log.info("User '{}' attempt to login, but user is disabled.", username);
            throw new UserDisabledException(username);
        }

        boolean isPwdCorrect = PasswordUtil.getValidator()
                .givenPasswordAndSalt(password, ue.getSalt())
                .compareToPasswordHash(ue.getPassword())
                .isMatched();
        if (!isPwdCorrect) {
            log.info("User '{}' attempt to login, but password is incorrect.", username);
            throw new PasswordIncorrectException(username);
        }

        log.info("User '{}' login successful, user_info returned", username);
        return cvtr.toVo(ue);
    }

    @Override
    public @NotNull UserVo login(@NotEmpty String username, @NotEmpty String password, @NotEmpty String appName)
            throws UserDisabledException, UsernameNotFoundException, PasswordIncorrectException,
            UserNotAllowedToUseApplicationException {

        // validate the credentials first
        UserVo uv = login(username, password);

        // validate whether current user is allowed to use this application
        if (!userAppService.isUserAllowedToUseApp(uv.getId(), appName)) {
            throw new UserNotAllowedToUseApplicationException(appName);
        }
        return uv;
    }

    @Override
    public void register(@NotNull RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException {
        Objects.requireNonNull(registerUserVo);
        Objects.requireNonNull(registerUserVo.getUsername());
        Objects.requireNonNull(registerUserVo.getPassword());
        Objects.requireNonNull(registerUserVo.getRole());

        if (userMapper.findIdByUsername(registerUserVo.getUsername()) != null) {
            log.info("Try to register user '{}', but username is already used.", registerUserVo.getUsername());
            throw new UserRegisteredException(registerUserVo.getUsername());
        }

        // limit the total number of administrators
        if (registerUserVo.getRole().equals(UserRole.ADMIN.getValue())) {
            checkAdminQuota();
        }
        User userEntity = toUserEntity(registerUserVo);
        userEntity.setIsDisabled(UserIsDisabled.NORMAL.getValue());

        log.info("New user '{}' successfully registered, role: {}", registerUserVo.getUsername(), registerUserVo.getRole().getValue());
        userMapper.insert(userEntity);
    }

    @Override
    public void requestRegistrationApproval(@NotNull RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException {
        Objects.requireNonNull(registerUserVo);
        Objects.requireNonNull(registerUserVo.getUsername());
        Objects.requireNonNull(registerUserVo.getPassword());
        Objects.requireNonNull(registerUserVo.getRole());

        if (userMapper.findIdByUsername(registerUserVo.getUsername()) != null) {
            log.info("Try to register user '{}', but username is already used.", registerUserVo.getUsername());
            throw new UserRegisteredException(registerUserVo.getUsername());
        }

        // limit the total number of administrators
        if (registerUserVo.getRole().equals(UserRole.ADMIN.getValue())) {
            checkAdminQuota();
        }

        // set user disabled, this will be handled by the admin
        User userEntity = toUserEntity(registerUserVo);
        userEntity.setIsDisabled(UserIsDisabled.DISABLED.getValue());
        userMapper.insert(userEntity);

        log.info("New user '{}' successfully registered, role: {}, currently disabled and waiting for approval",
                registerUserVo.getUsername(), registerUserVo.getRole().getValue());

        // generate a handling_event for registration request
        Objects.requireNonNull(userEntity.getId());
        eventHandlingService.createEvent(
                EventHandlingVo.builder()
                        .body(String.valueOf(userEntity.getId()))
                        .status(EventHandlingStatus.TO_BE_HANDLED.getValue())
                        .type(EventHandlingType.REGISTRATION_EVENT.getValue())
                        .build()
        );
        log.info("Created event_handling for {}'s registration", registerUserVo.getUsername());
    }

    @Override
    public void updatePassword(final String newPassword, final String oldPassword, long id) throws UserNotFoundException,
            PasswordIncorrectException {

        User ue = userMapper.findById(id);
        if (ue == null) {
            log.info("User_id '{}' attempt to change password, but user is not found.", id);
            throw new UserNotFoundException("user.id: " + id);
        }
        boolean isPasswordMatched = PasswordUtil.getValidator()
                .givenPasswordAndSalt(oldPassword, ue.getSalt())
                .compareToPasswordHash(ue.getPassword())
                .isMatched();
        if (!isPasswordMatched) {
            log.info("User_id '{}' attempt to change password, but the old password is unmatched.", id);
            throw new PasswordIncorrectException("user.id: " + id);
        }

        log.info("User_id '{}' successfully changed password.", id);
        userMapper.updatePwd(PasswordUtil.encodePassword(newPassword, ue.getSalt()), id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public @NotNull List<UserInfoVo> findNormalUserInfoList() {
        return BeanCopyUtils.toTypeList(userMapper.findNormalUserInfoList(), UserInfoVo.class);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public @NotNull List<UserInfoVo> findAllUserInfoList() {
        return BeanCopyUtils.toTypeList(userMapper.findAllUserInfoList(), UserInfoVo.class);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public @NotNull PageablePayloadSingleton<List<UserInfoVo>> findUserInfoByPage(@Valid @NotNull FindUserInfoVo vo) {
        User ue = new User();
        if (vo.getIsDisabled() != null)
            ue.setIsDisabled(vo.getIsDisabled().getValue());
        if (vo.getRole() != null)
            ue.setRole(vo.getRole().getValue());
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

    private User toUserEntity(RegisterUserVo registerUserVo) {
        User u = new User();
        u.setUsername(registerUserVo.getUsername());
        u.setRole(registerUserVo.getRole().getValue());
        u.setSalt(RandomNumUtil.randomNoStr(5));
        u.setPassword(PasswordUtil.encodePassword(registerUserVo.getPassword(), u.getSalt()));
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

    private void checkAdminQuota() throws ExceededMaxAdminCountException {
        // limit the total number of administrators
        Optional<Integer> optInt = parseInteger(environment.getProperty(ADMIN_LIMIT_COUNT_KEY));
        if (optInt.isPresent()) {
            int currCntOfAdmin = userMapper.countAdmin();
            // exceeded the max num of administrators
            if (currCntOfAdmin >= optInt.get()) {
                log.info("Try to register user as admin, but the maximum number of admin ({}) is exceeded.", optInt.get());
                throw new ExceededMaxAdminCountException(MessageFormat.format("Max: {0}, curr: {1}",
                        optInt.get(), currCntOfAdmin));
            }
        }
    }

}
