package com.curtisnewbie.service.auth.local.api;

import com.curtisnewbie.service.auth.dao.UserEntity;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.*;
import com.curtisnewbie.service.auth.remote.vo.RegisterUserVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test {@link LocalUserService}
 *
 * @author yongjie.zhuang
 */
@Rollback
@Transactional
@SpringBootTest
public class TestLocalUserService {

    private static final String USERNAME = "test_yongjie.zhuang";
    private static final String PASSWORD = "123123test";
    private static final String NEW_PASSWORD = "456456456test";
    private static final UserRole ROLE = UserRole.USER;
    private static final String CREATE_BY = "test case";


    @Autowired
    LocalUserService userService;

    @Test
    public void shouldRegister() {
        Assertions.assertDoesNotThrow(() -> {
            registerTestUser();
        });
    }

    @Test
    public void shouldLogin() {
        Assertions.assertDoesNotThrow(() -> {

            registerTestUser();

            UserVo vo = userService.login(USERNAME, PASSWORD);
            Assertions.assertNotNull(vo);
            Assertions.assertNotNull(vo.getId());
            Assertions.assertEquals(vo.getUsername(), USERNAME);
            Assertions.assertEquals(vo.getRole(), ROLE.getValue());
        });
    }

    @Test
    public void shouldUpdatePassword() {

        Assertions.assertDoesNotThrow(() -> {
            registerTestUser();

            UserVo vo = userService.login(USERNAME, PASSWORD);
            Assertions.assertNotNull(vo);
            Assertions.assertNotNull(vo.getId());
            int id = vo.getId();

            userService.updatePassword(NEW_PASSWORD, PASSWORD, id);

            vo = userService.login(USERNAME, NEW_PASSWORD);
            Assertions.assertNotNull(vo);
            Assertions.assertNotNull(vo.getId());
        });
    }

    @Test
    public void shouldFindNormalUserInfoList() {
        Assertions.assertNotNull(userService.findNormalUserInfoList());
    }

    @Test
    public void shouldFindAllUserInfoList() {
        Assertions.assertNotNull(userService.findAllUserInfoList());
    }

    @Test
    public void shouldDisableAndEnableUserById() {
        Assertions.assertDoesNotThrow(() -> {
            // create a test user
            registerTestUser();

            // try to login to get its id
            UserVo vo = userService.login(USERNAME, PASSWORD);
            Assertions.assertNotNull(vo);
            Assertions.assertNotNull(vo.getId());

            // disable it
            userService.disableUserById(vo.getId(), "test case");

            // test if it's disabled
            Assertions.assertThrows(UserDisabledException.class, () -> {
                userService.login(USERNAME, PASSWORD);
            });

            // enable it
            userService.enableUserById(vo.getId(), "test case");

            // login again
            Assertions.assertDoesNotThrow(() -> {
                UserVo voo = userService.login(USERNAME, PASSWORD);
                Assertions.assertNotNull(voo);
                Assertions.assertNotNull(voo.getId());
            });
        });
    }

    @Test
    public void shouldLoadUserByUsername() throws UsernameNotFoundException {
        Assertions.assertDoesNotThrow(() -> {
            registerTestUser();
        });

        UserEntity e = userService.loadUserByUsername(USERNAME);
        Assertions.assertNotNull(e);
        Assertions.assertNotNull(e.getId());
    }

    private void registerTestUser() throws UserRegisteredException, ExceededMaxAdminCountException {
        RegisterUserVo v = new RegisterUserVo();
        v.setUsername(USERNAME);
        v.setPassword(PASSWORD);
        v.setRole(ROLE);
        v.setCreateBy(CREATE_BY);
        userService.register(v);
    }
}
