package application.controllers;

import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.SettingsRequest;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import application.utils.responses.ErrorResponse;
import application.utils.responses.UserResponseWP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class SessionController {
    private static AccountService service;

    private static final String USER_ID = "userId";

    public SessionController(AccountService service) {
        SessionController.service = service;
    }

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody SignupRequest body, HttpSession httpSession) {
        final String error = Validator.checkSignup(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("User already authorize"));
        }

        if (!service.checkSignup(body.getLogin(), body.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Login or Email already exsists"));
        }

        final long id = service.addUser(body);
        httpSession.setAttribute(USER_ID, id);
        final User newUser = new User(id, body);
        return ResponseEntity.ok(new UserResponseWP(newUser));
    }

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity greetingSubmit(@RequestBody SigninRequest body, HttpSession httpSession) {
        final String error = Validator.checkSignin(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("User already authorize"));
        }

        final Long id = service.getId(body.getLogin());
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("User not found"));
        }

        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong login or password"));
        }

        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newpswrd", consumes = "application/json", produces = "application/json")
    public ResponseEntity setPassword(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong password"));
        }
        if (!Validator.checkPassword(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid new password"));
        }
        service.changePassword(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newlogin", consumes = "application/json", produces = "application/json")
    public ResponseEntity setLogin(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong password"));
        }
        if (!service.checkLogin(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Login already exsists"));
        }
        service.changeLogin(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newemail", consumes = "application/json", produces = "application/json")
    public ResponseEntity setEmail(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Wrong password"));
        }
        if (!service.checkEmail(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Email already exsists"));
        }
        service.changeEmail(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @GetMapping(path = "/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorize"));
        }
        httpSession.removeAttribute(USER_ID);
        return ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse("Successful"));
    }

    @GetMapping(path = "/user", produces = "application/json")
    public ResponseEntity user(HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }
}



