package com.curtisnewbie.service.auth.local.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.service.auth.dao.UserEntity;
import com.curtisnewbie.service.auth.dao.UserMapper;
import com.curtisnewbie.service.auth.local.api.LocalUserService;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.RegisterUserVo;
import com.curtisnewbie.service.auth.remote.vo.UserInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import com.curtisnewbie.service.auth.util.PasswordUtil;
import com.curtisnewbie.service.auth.util.RandomNumUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    public UserVo login(@NotEmpty String username, @NotEmpty String password) throws UserDisabledException, UsernameNotFoundException,
            PasswordIncorrectException {

        UserEntity ue = loadUserByUsername(username);
        if (ue == null) {
            logger.info("User '{}' attempt to login, but username is not found.", username);
            throw new UsernameNotFoundException(username);
        }
        UserIsDisabled isDisabled = EnumUtils.parse(ue.getIsDisabled(), UserIsDisabled.class);
        Objects.requireNonNull(isDisabled);
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
        Optional<Integer> optInt = parseInteger(environment.getProperty(ADMIN_LIMIT_COUNT_KEY));
        if (optInt.isPresent() && registerUserVo.getRole().equals(UserRole.ADMIN.getValue())) {
            int currCntOfAdmin = userMapper.countAdmin();
            // exceeded the max num of administrators
            if (currCntOfAdmin >= optInt.get()) {
                logger.info("Try to register user '{}' as admin, but the maximum number of admin ({}) is exceeded.",
                        registerUserVo.getUsername(), optInt.get());
                throw new ExceededMaxAdminCountException(MessageFormat.format("Max: {0}, curr: {1}",
                        optInt.get(), currCntOfAdmin));
            }
        }

        logger.info("New user '{}' successfully registered, role: {}", registerUserVo.getUsername(), registerUserVo.getRole().getValue());
        userMapper.insert(toUserEntity(registerUserVo));
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
    public List<UserInfoVo> findNormalUserInfoList() {
        return BeanCopyUtils.toTypeList(userMapper.findNormalUserInfoList(), UserInfoVo.class);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserInfoVo> findAllUserInfoList() {
        return BeanCopyUtils.toTypeList(userMapper.findAllUserInfoList(), UserInfoVo.class);
    }

    @Override
    public void disableUserById(int id, String disabledBy) {
        userMapper.disableUserById(id, disabledBy, new Date());
    }

    @Override
    public void enableUserById(int id, String enabledBy) {
        userMapper.enableUserById(id, enabledBy, new Date());
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

}
