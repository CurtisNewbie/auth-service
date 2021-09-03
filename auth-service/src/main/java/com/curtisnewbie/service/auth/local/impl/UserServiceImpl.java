package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.service.auth.dao.UserEntity;
import com.curtisnewbie.service.auth.dao.UserMapper;
import com.curtisnewbie.service.auth.local.api.LocalEventHandlingService;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yongjie.zhuang
 */
@DubboService(interfaceClass = RemoteUserService.class)
@Service
@Transactional
public class UserServiceImpl implements LocalUserService {
    private static final String ADMIN_LIMIT_COUNT_KEY = "admin.count.limit";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LocalEventHandlingService eventHandlingService;
    @Autowired
    private Environment environment;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserEntity loadUserByUsername(@NotEmpty String s) throws UsernameNotFoundException {
        Objects.requireNonNull(s);
        UserEntity userEntity = userMapper.findByUsername(s);
        if (userEntity == null)
            throw new UsernameNotFoundException("Username '" + s + "' not found");
        return userEntity;
    }

    @Override
    public String findUsernameById(int id) {
        return userMapper.findUsernameById(id);
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
        UserEntity ue = new UserEntity();
        ue.setId(param.getId());
        if (param.getIsDisabled() == null && param.getRole() == null)
            throw new IllegalArgumentException("Nothing to update");
        if (param.getIsDisabled() != null)
            ue.setIsDisabled(param.getIsDisabled().getValue());
        if (param.getRole() != null)
            ue.setRole(param.getRole().getValue());
        ue.setUpdateBy(param.getUpdateBy());
        ue.setUpdateTime(new Date());
        userMapper.updateUser(ue);
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
    public UserVo login(@NotEmpty String username, @NotEmpty String password) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException {

        UserEntity ue = loadUserByUsername(username);
        if (ue == null) {
            logger.info("User '{}' attempt to login, but username is not found.", username);
            throw new UsernameNotFoundException(username);
        }
        UserIsDisabled isDisabled = EnumUtils.parse(ue.getIsDisabled(), UserIsDisabled.class);
        Objects.requireNonNull(isDisabled, "Illegal is_disabled value");
        if (isDisabled == UserIsDisabled.DISABLED) {
            logger.info("User '{}' attempt to login, but user is disabled.", username);
            throw new UserDisabledException(username);
        }

        boolean isPwdCorrect = PasswordUtil.getValidator()
                .givenPasswordAndSalt(password, ue.getSalt())
                .compareToPasswordHash(ue.getPassword())
                .isMatched();
        if (!isPwdCorrect) {
            logger.info("User '{}' attempt to login, but password is incorrect.", username);
            throw new PasswordIncorrectException(username);
        }

        logger.info("User '{}' login successful, user_info returned", username);
        return BeanCopyUtils.toType(ue, UserVo.class);
    }

    @Override
    public void register(@NotNull RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException {
        Objects.requireNonNull(registerUserVo);
        Objects.requireNonNull(registerUserVo.getUsername());
        Objects.requireNonNull(registerUserVo.getPassword());
        Objects.requireNonNull(registerUserVo.getRole());

        if (userMapper.findIdByUsername(registerUserVo.getUsername()) != null) {
            logger.info("Try to register user '{}', but username is already used.", registerUserVo.getUsername());
            throw new UserRegisteredException(registerUserVo.getUsername());
        }

        // limit the total number of administrators
        if (registerUserVo.getRole().equals(UserRole.ADMIN.getValue())) {
            checkAdminQuota();
        }
        UserEntity userEntity = toUserEntity(registerUserVo);
        userEntity.setIsDisabled(UserIsDisabled.NORMAL.getValue());

        logger.info("New user '{}' successfully registered, role: {}", registerUserVo.getUsername(), registerUserVo.getRole().getValue());
        userMapper.insert(userEntity);
    }

    @Override
    public void requestRegistrationApproval(@NotNull RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException {
        Objects.requireNonNull(registerUserVo);
        Objects.requireNonNull(registerUserVo.getUsername());
        Objects.requireNonNull(registerUserVo.getPassword());
        Objects.requireNonNull(registerUserVo.getRole());

        if (userMapper.findIdByUsername(registerUserVo.getUsername()) != null) {
            logger.info("Try to register user '{}', but username is already used.", registerUserVo.getUsername());
            throw new UserRegisteredException(registerUserVo.getUsername());
        }

        // limit the total number of administrators
        if (registerUserVo.getRole().equals(UserRole.ADMIN.getValue())) {
            checkAdminQuota();
        }

        // set user disabled, this will be handled by the admin
        UserEntity userEntity = toUserEntity(registerUserVo);
        userEntity.setIsDisabled(UserIsDisabled.DISABLED.getValue());
        userMapper.insert(userEntity);

        logger.info("New user '{}' successfully registered, role: {}, currently disabled and waiting for approval",
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
        logger.info("Created event_handling for {}'s registration", registerUserVo.getUsername());
    }

    @Override
    public void updatePassword(final String newPassword, final String oldPassword, long id) throws UserNotFoundException,
            PasswordIncorrectException {

        UserEntity ue = userMapper.findById(id);
        if (ue == null) {
            logger.info("User_id '{}' attempt to change password, but user is not found.", id);
            throw new UserNotFoundException("user.id: " + id);
        }
        boolean isPasswordMatched = PasswordUtil.getValidator()
                .givenPasswordAndSalt(oldPassword, ue.getSalt())
                .compareToPasswordHash(ue.getPassword())
                .isMatched();
        if (!isPasswordMatched) {
            logger.info("User_id '{}' attempt to change password, but the old password is unmatched.", id);
            throw new PasswordIncorrectException("user.id: " + id);
        }

        logger.info("User_id '{}' successfully changed password.", id);
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
    public @NotNull PageInfo<UserInfoVo> findUserInfoByPage(@NotNull FindUserInfoVo vo) {
        Objects.requireNonNull(vo.getPagingVo());
        PageHelper.startPage(vo.getPagingVo().getPage(), vo.getPagingVo().getLimit());
        UserEntity ue = new UserEntity();
        if (vo.getIsDisabled() != null)
            ue.setIsDisabled(vo.getIsDisabled().getValue());
        if (vo.getRole() != null)
            ue.setRole(vo.getRole().getValue());
        ue.setUsername(vo.getUsername());
        return BeanCopyUtils.toPageList(PageInfo.of(userMapper.findUserInfoBy(ue)), UserInfoVo.class);
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

    private UserEntity toUserEntity(RegisterUserVo registerUserVo) {
        UserEntity u = new UserEntity();
        u.setUsername(registerUserVo.getUsername());
        u.setRole(registerUserVo.getRole().getValue());
        u.setSalt(RandomNumUtil.randomNoStr(5));
        u.setPassword(PasswordUtil.encodePassword(registerUserVo.getPassword(), u.getSalt()));
        u.setCreateBy(registerUserVo.getCreateBy());
        u.setCreateTime(new Date());
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
                logger.info("Try to register user as admin, but the maximum number of admin ({}) is exceeded.", optInt.get());
                throw new ExceededMaxAdminCountException(MessageFormat.format("Max: {0}, curr: {1}",
                        optInt.get(), currCntOfAdmin));
            }
        }
    }

}
