package application.controllers;

import application.models.User;
import application.services.AccountService;
import application.utils.ControllerHelper;
import application.utils.Validator;
import application.utils.requests.SettingsRequest;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import application.utils.responses.MessageResponse;
import application.utils.responses.UserResponseWP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "https://gametes.herokuapp.com/")
public class SessionController {
    private AccountService service;
    private ControllerHelper helper;

    private static final String USER_ID = "userId";
    private static final String JSON = "application/json";

    public SessionController(AccountService service, ControllerHelper helper) {
        this.service = service;
        this.helper = helper;
    }

    @PostMapping(path = "/signup", consumes = JSON, produces = JSON)
    public ResponseEntity signup(@RequestBody SignupRequest body, HttpSession httpSession) {
        final ResponseEntity error = helper.signupCheck(body, httpSession, service);
        if (error != null) {
            return error;
        }
        final Long id = service.addUser(body);
        httpSession.setAttribute(USER_ID, id);
        final User newUser = new User(id, body);
        return ResponseEntity.ok(new UserResponseWP(newUser));
    }

    @PostMapping(path = "/signin", consumes = JSON, produces = JSON)
    public ResponseEntity greetingSubmit(@RequestBody SigninRequest body, HttpSession httpSession) {
        final Long id = service.getId(body.getLogin());
        final ResponseEntity error = helper.signinCheck(body, httpSession, service, id);
        if (error != null) {
            return error;
        }
        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newpswrd", consumes = JSON, produces = JSON)
    public ResponseEntity setPassword(@RequestBody SettingsRequest body, HttpSession httpSession) {
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        final ResponseEntity error = helper.newCheck(body, httpSession, service, id);
        if (error != null) {
            return error;
        }
        if (!Validator.checkPassword(body.getFieldToChange()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid new password"));
        }
        service.changePassword(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newlogin", consumes = JSON, produces = JSON)
    public ResponseEntity setLogin(@RequestBody SettingsRequest body, HttpSession httpSession) {
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        final ResponseEntity error = helper.newCheck(body, httpSession, service, id);
        if (error != null) {
            return error;
        }
        if (!service.checkLogin(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Login already exsists"));
        }
        service.changeLogin(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newemail", consumes = JSON, produces = JSON)
    public ResponseEntity setEmail(@RequestBody SettingsRequest body, HttpSession httpSession) {
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        final ResponseEntity error = helper.newCheck(body, httpSession, service, id);
        if (error != null) {
            return error;
        }
        if (!service.checkEmail(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email already exsists"));
        }
        service.changeEmail(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @GetMapping(path = "/logout", produces = JSON)
    public ResponseEntity logout(HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User not authorize"));
        }
        httpSession.removeAttribute(USER_ID);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Successful"));
    }

    @GetMapping(path = "/user", produces = JSON)
    public ResponseEntity user(HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }
}



