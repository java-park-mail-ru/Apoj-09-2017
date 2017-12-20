package application.service;

import application.models.User;
import application.services.AccountService;
import application.utils.requests.SignupRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class AccountServiceTest {
    static final String LOGIN = "login";
    static final String EMAIL = "email@mail.ru";
    static final String PASSWORD = "qwerty123";

    @Autowired
    private AccountService service;
    @Autowired
    private PasswordEncoder encoder;

    private long testUserId;

    @Before
    public void before() {
        final SignupRequest user = new SignupRequest(LOGIN, PASSWORD, EMAIL);
        testUserId = service.addUser(user);
    }

    @Test
    public void testAddUser() {
        final SignupRequest user = new SignupRequest("qghnghn", "qfnhfn565756fgh", "qfghf@mail.ru");
        final long id = service.addUser(user);
        assertTrue(id > 0);
    }

    @Test
    public void testGetUserById() {
        final User user = service.getUser(testUserId);
        assertEquals(user.getId(), testUserId);
        assertEquals(user.getEmail(), EMAIL);
        assertEquals(user.getLogin(), LOGIN);
        assertTrue(encoder.matches(PASSWORD, user.getPassword()));
    }

    @Test
    public void testGetUserByLogin() {
        final User user = service.getUser(LOGIN);
        assertEquals(user.getId(), testUserId);
        assertEquals(user.getEmail(), EMAIL);
        assertEquals(user.getLogin(), LOGIN);
    }

    @Test
    public void testCheckLogin() {
        assertFalse(service.checkLogin(LOGIN));
        assertTrue(service.checkLogin("dfghjjfnjdfnvj"));
    }

    @Test
    public void testCheckEmail() {
        assertFalse(service.checkEmail(EMAIL));
        assertTrue(service.checkEmail("dfghjjfnjdfnvj@mail.dh"));
    }

    @Test
    public void testCheckSignup() {
        assertFalse(service.checkSignup(LOGIN, EMAIL));
        assertFalse(service.checkSignup(LOGIN, "fvejfnve@jf.v"));
        assertFalse(service.checkSignup("sfjnjsfv", EMAIL));
        assertTrue(service.checkSignup("login123", "dfghjjfnjdfnvj@mail.dh"));
    }

    @Test
    public void testCheckSignin() {
        assertFalse(service.checkSignin(testUserId + 1, "fbhfbh7438473b"));
        assertFalse(service.checkSignin(testUserId, "2313hdbch"));
        assertFalse(service.checkSignin(testUserId + 1, PASSWORD));
        assertTrue(service.checkSignin(testUserId, PASSWORD));
    }

    @After
    public void clear() {
        service.clear();
    }

}
