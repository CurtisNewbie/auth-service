package com.curtisnewbie.service.auth.local.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.curtisnewbie.common.dao.IsDel;
import com.curtisnewbie.common.util.*;
import com.curtisnewbie.common.vo.*;
import com.curtisnewbie.goauth.client.GoAuthClient;
import com.curtisnewbie.goauth.client.RoleInfoReq;
import com.curtisnewbie.goauth.client.RoleInfoResp;
import com.curtisnewbie.module.jwt.domain.api.JwtBuilder;
import com.curtisnewbie.module.jwt.domain.api.JwtDecoder;
import com.curtisnewbie.module.jwt.vo.DecodeResult;
import com.curtisnewbie.module.redisutil.RedisController;
import com.curtisnewbie.service.auth.dao.User;
import com.curtisnewbie.service.auth.infrastructure.repository.mapper.UserMapper;
import com.curtisnewbie.service.auth.local.api.*;
import com.curtisnewbie.service.auth.remote.consts.ReviewStatus;
import com.curtisnewbie.service.auth.remote.consts.UserIsDisabled;
import com.curtisnewbie.service.auth.remote.vo.*;
import com.curtisnewbie.service.auth.util.PasswordUtil;
import com.curtisnewbie.service.auth.web.open.api.vo.ListUserReq;
import com.curtisnewbie.service.auth.web.open.api.vo.UserWebVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import static com.curtisnewbie.common.trace.TraceUtils.tUser;
import static com.curtisnewbie.common.util.AssertUtils.*;
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
public class UserServiceImpl implements UserService {

    private static final String USER_NO_PREFIX = "UE";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserKeyService userKeyService;
    @Autowired
    private JwtBuilder jwtBuilder;
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private RedisController redisController;
    @Autowired
    private GoAuthClient goAuthClient;

    @Override
    public User loadUserByUsername(String s) {
        Objects.requireNonNull(s);
        User user = userMapper.findByUsername(s);
        notNull(user, USER_NOT_FOUND);
        return user;
    }

    @Override
    public UserWebVo loadUserInfo(String username) {
        AssertUtils.hasText(username, "username is empty");
        User user = userMapper.findByUsername(username);
        notNull(user, USER_NOT_FOUND);

        UserWebVo uw = new UserWebVo();
        uw.setUserNo(user.getUserNo());
        uw.setServices(Collections.emptyList());
        uw.setId(user.getId());
        uw.setRoleNo(user.getRoleNo());
        uw.setUsername(user.getUsername());
        return uw;
    }

    @Override
    public String findUsernameById(int id) {
        return userMapper.findUsernameById(id);
    }

    @Override
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
    public void updateUser(UpdateUserVo param) {
        User ue = new User();
        ue.setId(param.getId());
        ue.setIsDisabled(param.getIsDisabled());
        ue.setRoleNo(param.getRoleNo());
        ue.setUpdateBy(param.getUpdateBy());
        ue.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(ue);
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
        final User ue = checkLoginStatus(username);
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
    public String exchangeToken(String token) {
        DecodeResult decodeResult = jwtDecoder.decode(token);
        isTrue(decodeResult.isValid(), TOKEN_EXPIRED);

        final String idAsStr = decodeResult.getDecodedJWT().getClaim("id").asString();
        final User ue = userMapper.findById(Long.parseLong(idAsStr));
        return buildToken(BeanCopyUtils.toType(ue, UserVo.class));
    }

    @Override
    public User getUserInfoByToken(@NotEmpty String token) {
        DecodeResult decodeResult = jwtDecoder.decode(token);
        isTrue(decodeResult.isValid(), TOKEN_EXPIRED);

        final long id = decodeResult.getDecodedJWT().getClaim("id").asLong();
        User user = userMapper.findById(id);
        notNull(user, USER_NOT_FOUND);
        user.setPassword(null);

        AssertUtils.isFalse(user.isDeleted(), USER_NOT_FOUND);
        AssertUtils.equals(user.getIsDisabled(), UserIsDisabled.NORMAL, USER_DISABLED);
        return user;
    }

    @Override
    public void reviewUserRegistration(UserReviewCmd cmd) {
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
            update.setReviewStatus(reviewStatus);
            update.setIsDisabled(
                    reviewStatus == ReviewStatus.APPROVED ? UserIsDisabled.NORMAL : UserIsDisabled.DISABLED);
            userMapper.updateById(update);
        });
    }

    @Override
    public UserVo login(String username, String password) {
        final User user = userLogin(username, password);
        log.info("User '{}' login successful, user_info returned", username);
        return BeanCopyUtils.toType(user, UserVo.class);
    }

    @Override
    public void adminAddUser(AddUserVo req) {
        // validate whether username and password is entered
        final String username = req.getUsername();
        hasText(username, "Username is required");
        validateUsername(username);

        final String password = req.getPassword();
        hasText(password, "Password is required");
        validatePassword(password);

        // validate if the username and password is the same
        notEquals(username, password, "Username and password must be different");

        // user is already registered
        isNull(userMapper.findIdByUsername(req.getUsername()), USER_ALREADY_REGISTERED);

        // save user
        User user = prepNewUserCred(req.getPassword());
        user.setUserNo(genUserNo());
        user.setUsername(req.getUsername());
        user.setRoleNo(req.getRoleNo());
        user.setCreateBy(tUser().getUsername());
        user.setCreateTime(LocalDateTime.now());
        user.setIsDisabled(UserIsDisabled.NORMAL);
        user.setReviewStatus(ReviewStatus.APPROVED); // users added by admin, review not needed

        log.info("New user '{}' successfully registered, roleNo: {}", req.getUsername(), req.getRoleNo());
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

        // build user (without role)
        User user = prepNewUserCred(v.getPassword());
        user.setUserNo(genUserNo());
        user.setUsername(v.getUsername());

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
    public PageableList<UserInfoVo> findUserInfoByPage(ListUserReq req) {
        IPage<User> pge = userMapper.findUserInfoBy(forPage(req.getPagingVo()), req);
        final PageableList<UserInfoVo> pl = toPageableList(pge, v -> BeanCopyUtils.toType(v, UserInfoVo.class));

        // convert roleNo to role name
        if (pl.getPayload() != null) {
            try {
                pl.getPayload().stream()
                        .filter(u -> StringUtils.hasText(u.getRoleNo()))
                        .forEach(u -> {
                            final RoleInfoReq r = new RoleInfoReq();
                            r.setRoleNo(u.getRoleNo());
                            final Result<RoleInfoResp> res = goAuthClient.getRoleInfo(r);
                            if (res.isOk())
                                u.setRoleName(res.getData().getName());
                        });
            } catch (Exception e) {
                log.error("Failed to fetch role names", e);
            }
        }
        return pl;
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
        if (req.getUserId() != null)
            user = userMapper.findById(req.getUserId());
        else if (req.getUserNo() != null)
            user = userMapper.selectOneEq(User::getUserNo, req.getUserNo());
        else if (req.getUsername() != null)
            user = userMapper.selectOneEq(User::getUsername, req.getUsername());
        else
            return null;
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
        claims.put("roleno", user.getRoleNo());
        claims.put("services", "");

        // by default valid for 15 minutes
        return jwtBuilder.encode(claims, LocalDateTime.now().plusMinutes(15));
    }

    private User userLogin(String username, String password) {
        final User ue = checkLoginStatus(username);

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

    private User checkLoginStatus(String username) {
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
