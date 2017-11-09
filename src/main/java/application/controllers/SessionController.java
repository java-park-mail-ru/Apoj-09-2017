package application.controllers;

import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.SettingsRequest;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import application.utils.responses.MessageResponse;
import application.utils.responses.UserResponseWP;
import application.utils.Messages;
import application.utils.responses.ValidatorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RestController
@CrossOrigin //(origins = {"https://gametes.herokuapp.com", "http://www.apoj.me"})
public class SessionController {
    private AccountService service;
    public static final String JSON = "application/json";
    public static final String USER_ID = "userId";

    public SessionController(AccountService service) {
        this.service = service;
    }

    @PostMapping(path = "/signup", consumes = JSON, produces = JSON)
    public ResponseEntity signup(@RequestBody SignupRequest body, HttpSession httpSession) {
        final ArrayList<String> error = Validator.checkSignup(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidatorResponse(error));
        }
        if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(Messages.AUTHORIZED));
        }
        if (!service.checkSignup(body.getLogin(), body.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(Messages.EXISTS));
        }
        final Long id = service.addUser(body);
        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new UserResponseWP(id, body));
    }

    @PostMapping(path = "/signin", consumes = JSON, produces = JSON)
    public ResponseEntity signin(@RequestBody SigninRequest body, HttpSession httpSession) {
        final ArrayList<String> error = Validator.checkSignin(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidatorResponse(error));
        }
        if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(Messages.AUTHORIZED));
        }
        final User user = service.getUser(body.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(Messages.WRONG_LOGIN_PASSWORD));
        }
        final long userId = user.getId();
        if (!service.checkSignin(userId, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(Messages.WRONG_LOGIN_PASSWORD));
        }
        httpSession.setAttribute(USER_ID, userId);
        return ResponseEntity.ok(new UserResponseWP(user));
    }

    @PostMapping(path = "/newpassword", consumes = JSON, produces = JSON)
    public ResponseEntity setPassword(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(Messages.NOT_AUTHORIZE));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(Messages.BAD_COOKIE));
        }
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(Messages.WRONG_PASSWORD));
        }
        final ArrayList<String> error = Validator.checkPassword(body.getPassword());
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidatorResponse(error));
        }
        service.changePassword(user, body.getFieldToChange());
        return ResponseEntity.ok(new UserResponseWP(user));
    }

    @PostMapping(path = "/newlogin", consumes = JSON, produces = JSON)
    public ResponseEntity setLogin(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(Messages.NOT_AUTHORIZE));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(Messages.BAD_COOKIE));
        }
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(Messages.WRONG_PASSWORD));
        }
        if (!service.checkLogin(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(Messages.LOGIN_EXISTS));
        }
        service.changeLogin(user, body.getFieldToChange());
        return ResponseEntity.ok(new UserResponseWP(user));
    }

    @PostMapping(path = "/newemail", consumes = JSON, produces = JSON)
    public ResponseEntity setEmail(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(Messages.NOT_AUTHORIZE));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(Messages.BAD_COOKIE));
        }
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(Messages.WRONG_PASSWORD));
        }
        if (!service.checkEmail(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse(Messages.EMAIL_EXISTS));
        }
        service.changeEmail(user, body.getFieldToChange());
        return ResponseEntity.ok(new UserResponseWP(user));
    }

    @PostMapping(path = "/logout", produces = JSON)
    public ResponseEntity logout(HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(Messages.NOT_AUTHORIZE));
        }
        httpSession.removeAttribute(USER_ID);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(Messages.SUCCESS));
    }

    @GetMapping(path = "/user", produces = JSON)
    public ResponseEntity user(HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(Messages.NOT_AUTHORIZE));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        final User user = service.getUser(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse(Messages.BAD_COOKIE));
        }
        return ResponseEntity.ok(new UserResponseWP(user));
    }
}
