package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.dao.IsDel;
import com.curtisnewbie.common.exceptions.UnrecoverableException;
import com.curtisnewbie.common.util.*;
import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.module.jwt.domain.api.JwtBuilder;
import com.curtisnewbie.module.jwt.domain.api.JwtDecoder;
import com.curtisnewbie.module.jwt.vo.DecodeResult;
import com.curtisnewbie.module.redisutil.RedisController;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.infrastructure.converters.UserConverter;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserMapper;
import com.curtisnewbie.service.auth.local.api.*;
import com.curtisnewbie.service.auth.remote.consts.ReviewStatus;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.util.PasswordUtil;
import com.curtisnewbie.service.auth.web.open.api.vo.UserWebVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import static com.curtisnewbie.common.trace.TraceUtils.tUser;
import static com.curtisnewbie.common.util.AssertUtils.*;
import static com.curtisnewbie.common.util.MapperUtils.selectListAndConvert;
import static com.curtisnewbie.common.util.PagingUtil.*;
import static com.curtisnewbie.common.util.RandomUtils.sequence;
import static com.curtisnewbie.service.auth.remote.consts.AuthServiceError.*;
import static com.curtisnewbie.service.auth.util.PasswordUtil.encodePassword;
import static com.curtisnewbie.service.auth.util.UserValidator.validatePassword;
import static com.curtisnewbie.service.auth.util.UserValidator.validateUsername;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final String ADMIN_LIMIT_COUNT_KEY = "admin.count.limit";
    private static final String USER_NO_PREFIX = "UE";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserKeyService userKeyService;
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
    @Autowired
    private RedisController redisController;
    @Autowired
    private LocalAppService appService;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User loadUserByUsername(String s) {
        Objects.requireNonNull(s);
        User user = userMapper.findByUsername(s);
        notNull(user, USER_NOT_FOUND);
        return user;
    }

    @Override
    public UserWebVo loadUserInfo(String username) {

        User user = userMapper.findByUsername(username);
        notNull(user, USER_NOT_FOUND);

        final List<String> appNames = userAppService.getAppsPermittedForUser(user.getId())
                .stream()
                .map(AppBriefVo::getName).collect(Collectors.toList());
        UserWebVo uw = new UserWebVo();
        uw.setServices(appNames);
        uw.setId(user.getId());
        uw.setRole(user.getRole());
        uw.setUsername(user.getUsername());

        return uw;
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
    public String exchangeToken(String username, String password, String appName) {
        final UserVo user = login(username, password, appName);
        return buildToken(user);
    }

    @Override
    public String exchangeToken(@NotEmpty String token) {
        DecodeResult decodeResult = jwtDecoder.decode(token);
        isTrue(decodeResult.isValid(), TOKEN_EXPIRED);

        final String idAsStr = decodeResult.getDecodedJWT().getClaim("id").asString();
        final User ue = userMapper.findById(Long.parseLong(idAsStr));
        return buildToken(cvtr.toVo(ue));
    }

    @Override
    public User getUserInfoByToken(@NotEmpty String token) {
        DecodeResult decodeResult = jwtDecoder.decode(token);
        isTrue(decodeResult.isValid(), TOKEN_EXPIRED);

        final String idAsStr = decodeResult.getDecodedJWT().getClaim("id").asString();
        User user = userMapper.findById(Long.parseLong(idAsStr));
        notNull(user, USER_NOT_FOUND);

        user.setPassword(null);

        AssertUtils.isFalse(user.isDeleted(), USER_NOT_FOUND);
        AssertUtils.equals(user.getIsDisabled(), UserIsDisabled.NORMAL, USER_DISABLED);
        return user;
    }

    @Override
    public void reviewUserRegistration(@NotNull UserReviewCmd cmd) {
        final Integer userId = cmd.getUserId();
        notNull(userId, "user_id == null");
        final ReviewStatus reviewStatus = cmd.getReviewStatus();
        notNull(reviewStatus, "review_status == null");
        isTrue(reviewStatus.isDecision(), "Illegal Argument");

        final Lock lock = redisController.getLock("auth:user:registration:review:" + userId);
        LockUtils.lockAndRun(lock, () -> {
            final User user = userMapper.selectById(userId);
            AssertUtils.notNull(user, USER_NOT_FOUND);
            AssertUtils.isFalse(user.isDeleted(), USER_NOT_FOUND);
            AssertUtils.isTrue(user.getReviewStatus() == ReviewStatus.PENDING,
                    "User's registration has already been reviewed");

            User update = new User();
            update.setId(userId);
            update.setUpdateBy(tUser().getUsername());
            update.setUpdateTimeIfAbsent();
            update.setReviewStatus(reviewStatus);
            update.setIsDisabled(reviewStatus == ReviewStatus.APPROVED ? UserIsDisabled.NORMAL : UserIsDisabled.DISABLED);
            userMapper.updateById(update);

            if (reviewStatus == ReviewStatus.APPROVED) {
                final Integer appId = appService.getAppIdByName("auth-service");
                if (appId != null)
                    userAppService.addUserApp(userId, appId, "system");
            }
        });
    }

    @Override
    public List<Integer> listEmptyUserNoId() {
        final LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getUserNo, "");

        return selectListAndConvert(qw, userMapper, User::getId);
    }

    @Override
    public void generateUserNoIfEmpty(int id) {
        User u = new User();
        u.setUserNo(genUserNo());

        userMapper.update(u, new LambdaQueryWrapper<User>()
                .eq(User::getId, id)
                .eq(User::getUserNo, ""));
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
    public void addUser(AddUserVo addUserVo) {
        // validate whether username and password is entered
        final String username = addUserVo.getUsername();
        hasText(username, "Username is required");
        validateUsername(username);

        final String password = addUserVo.getPassword();
        hasText(password, "Password is required");
        validatePassword(password);

        // validate if the username and password is the same
        notEquals(username, password, "Username and password must be different");

        // if not specified, the role will be guest
        if (addUserVo.getRole() == null)
            addUserVo.setRole(UserRole.GUEST);

        // do not support adding administrator
        isFalse(addUserVo.isAdmin(), "Do not support adding administrator");

        // user is already registered
        isNull(userMapper.findIdByUsername(addUserVo.getUsername()), USER_ALREADY_REGISTERED);

        // save user
        User user = prepNewUserCred(addUserVo.getPassword());
        user.setUserNo(genUserNo());
        user.setUsername(addUserVo.getUsername());
        user.setRole(addUserVo.getRole());
        user.setCreateBy(tUser().getUsername());
        user.setCreateTime(LocalDateTime.now());
        user.setIsDisabled(UserIsDisabled.NORMAL);

        log.info("New user '{}' successfully registered, role: {}", addUserVo.getUsername(), addUserVo.getRole().getValue());
        userMapper.insert(user);
    }

    @Override
    public void register(RegisterUserVo v) {
        final String username = v.getUsername();
        hasText(username, "Please enter username");
        validateUsername(username);

        final String password = v.getPassword();
        hasText(password, "Please enter password");

        // validate if the username and password are the same
        notEquals(v.getUsername(), password, "Username and password must be different");

        // validate if the password is too short
        validatePassword(v.getPassword());

        // user is already registered
        isNull(userMapper.findIdByUsername(v.getUsername()), USER_ALREADY_REGISTERED);

        // build user
        User user = prepNewUserCred(v.getPassword());
        user.setUserNo(genUserNo());
        user.setUsername(v.getUsername());
        user.setRole(UserRole.GUEST);
        user.setCreateTime(LocalDateTime.now());
        user.setCreateBy(v.getUsername());

        // user will be reviewed by admin
        user.setIsDisabled(UserIsDisabled.DISABLED);
        user.setReviewStatus(ReviewStatus.PENDING);
        userMapper.insert(user);

        log.info("New user '{}' successfully registered, waiting for approval", v.getUsername());
    }

    @Override
    public void updatePassword(final String newPassword, final String oldPassword, long userId) {
        final User ue = userMapper.findById(userId);
        notNull(ue, USER_NOT_FOUND);

        final boolean isPasswordMatched = PasswordUtil.getValidator(ue)
                .givenPassword(oldPassword)
                .isMatched();
        isTrue(isPasswordMatched, PASSWORD_INCORRECT);

        final boolean isUpdated = userMapper.updatePwd(encodePassword(newPassword, ue.getSalt()), userId) > 0;
        if (isUpdated)
            log.info("User_id '{}' successfully changed password.", userId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PageableList<UserInfoVo> findUserInfoByPage(FindUserInfoVo vo) {
        User ue = new User();
        if (vo.getIsDisabled() != null)
            ue.setIsDisabled(vo.getIsDisabled());
        if (vo.getRole() != null)
            ue.setRole(vo.getRole());

        ue.setUsername(vo.getUsername());

        IPage<User> pge = userMapper.findUserInfoBy(forPage(vo.getPagingVo()), ue);
        return toPageableList(pge, cvtr::toInfoVo);
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

    @Override
    public UserInfoVo getUserInfo(int userId) {
        User user = userMapper.findById(userId);
        notNull(user, USER_NOT_FOUND);

        user.setPassword(null);
        return BeanCopyUtils.toType(user, UserInfoVo.class);
    }

    @Override
    public UserInfoVo getUserInfo(String userNo) {
        User user = userMapper.selectOneEq(User::getUserNo, userNo);
        notNull(user, USER_NOT_FOUND);

        user.setPassword(null);
        return BeanCopyUtils.toType(user, UserInfoVo.class);

    }

    @Override
    public Map<String, String> fetchUsernameByUserNos(List<String> userNos) {
        userNos = userNos.stream().distinct().collect(Collectors.toList());
        return userMapper.selectList(MapperUtils.select(User::getUserNo, User::getUsername)
                        .in(User::getUserNo, userNos)
                        .eq(User::getIsDel, IsDel.NORMAL))
                .stream()
                .collect(Collectors.toMap(User::getUserNo, User::getUsername));
    }

    @Override
    public UserInfoVo findUser(FindUserReq req) {
        User user;
        if (req.getUserId() != null) user = userMapper.findById(req.getUserId());
        else if (req.getUserNo() != null) user = userMapper.selectOneEq(User::getUserNo, req.getUserNo());
        else if (req.getUsername() != null) user = userMapper.selectOneEq(User::getUsername, req.getUsername());
        else return null;
        notNull(user, USER_NOT_FOUND);

        user.setPassword(null);
        return BeanCopyUtils.toType(user, UserInfoVo.class);
    }

    // ---------------------- private helper methods -----------------

    private String buildToken(UserVo user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("id", user.getId().toString());
        claims.put("username", user.getUsername());
        claims.put("userno", user.getUserNo());
        claims.put("role", user.getRole().getValue());

        final String appNames = userAppService.getAppsPermittedForUser(user.getId())
                .stream()
                .map(AppBriefVo::getName)
                .collect(Collectors.joining(","));
        claims.put("services", appNames);

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
        if (!isPwdCorrect)
            isTrue(userKeyService.isUserKeyValid(ue.getId(), password), PASSWORD_INCORRECT);
        return ue;
    }

    /** Prepare new user object's credential (salt and encoded password) */
    private static User prepNewUserCred(String password) {
        User u = new User();
        u.setSalt(RandomUtils.randomNumeric(5));
        u.setPassword(encodePassword(password, u.getSalt()));
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

        // logically deleted
        isFalse(ue.isDeleted(), USER_NOT_FOUND);

        // waiting for approval
        isFalse(ue.getReviewStatus() == ReviewStatus.PENDING, REG_REVIEW_PENDING);

        // registration is rejected
        isFalse(ue.getReviewStatus() == ReviewStatus.REJECTED, REG_REVIEW_REJECTED);

        // user disabled
        isTrue(ue.getIsDisabled() == UserIsDisabled.NORMAL, USER_DISABLED);
        return ue;
    }

    private static String genUserNo() {
        return sequence(USER_NO_PREFIX, 7);
    }
}
