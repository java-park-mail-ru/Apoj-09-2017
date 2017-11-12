package application.service;

import application.services.AccountService;
import application.utils.requests.SignupRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServiceTest {
    static final String LOGIN = "login";
    static final String EMAIL = "email@mail.ru";
    static final String PASSWORD = "qwerty123";

    @Autowired
    private AccountService usersService;
    @Autowired
    private TestRestTemplate template;

    private HttpEntity<Object> getHttpEntity(@NotNull Object body, @Nullable List<String> cookie) {
        if (cookie != null) {
            final HttpHeaders headers = new HttpHeaders();
            headers.put("Cookie", cookie);
            return new HttpEntity<>(body, headers);
        } else {
            return new HttpEntity<>(body);
        }
    }

    private @Nullable List<String> signup(@NotNull String login, @NotNull String email, @NotNull String password,
                                          @NotNull HttpStatus status, @Nullable List<String> cookie) {

        final SignupRequest signupRequest = new SignupRequest(login, password, email);
        final HttpEntity<Object> entity = getHttpEntity(signupRequest, cookie);

        final ResponseEntity<String> response = template.exchange("/signup", HttpMethod.POST, entity, String.class);
        Assert.assertEquals(status, response.getStatusCode());

        return response.getHeaders().get("Set-Cookie");
    }

    @Test
    public void testFullRegistration() {
        signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);

    }

    @Test
    public void testConflictSignup() {
        signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);
        signup(LOGIN, EMAIL, PASSWORD, HttpStatus.CONFLICT, null);
    }

    @Test
    public void testCorrectSignup() {
        signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);
    }

    @Test
    public void testAuthorizedSignup() {
        final List<String> cookie = signup(LOGIN, EMAIL, PASSWORD, HttpStatus.OK, null);
        signup("uniq", "uniq@mail.ru", PASSWORD, HttpStatus.FORBIDDEN, cookie);
    }

    @Test
    public void testBadRequestSignup() {
        signup("", EMAIL, PASSWORD, HttpStatus.BAD_REQUEST, null);
    }

    @After
    public void clear() {
        usersService.clear();
    }

}
