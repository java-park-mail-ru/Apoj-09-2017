package application.controllers;

import application.models.User;
import application.services.AccountService;
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

    private static final String USER_ID = "userId";
    private static final String JSON = "application/json";

    public SessionController(AccountService service) {
        this.service = service;
    }

    @PostMapping(path = "/signup", consumes = JSON, produces = JSON)
    public ResponseEntity signup(@RequestBody SignupRequest body, HttpSession httpSession) {
        final String error = Validator.checkSignup(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("User already authorize"));
        }

        if (!service.checkSignup(body.getLogin(), body.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Login or Email already exists"));
        }

        final Long id = service.addUser(body);
        httpSession.setAttribute(USER_ID, id);
        final User newUser = new User(id, body);
        return ResponseEntity.ok(new UserResponseWP(newUser));
    }

    @PostMapping(path = "/signin", consumes = JSON, produces = JSON)
    public ResponseEntity greetingSubmit(@RequestBody SigninRequest body, HttpSession httpSession) {
        final String error = Validator.checkSignin(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("User already authorize"));
        }

        final Long id = service.getId(body.getLogin());
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));
        }

        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Wrong login or password"));
        }

        httpSession.setAttribute(USER_ID, id);
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newpswrd", consumes = JSON, produces = JSON)
    public ResponseEntity setPassword(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Wrong password"));
        }
        if (!Validator.checkPassword(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid new password"));
        }
        service.changePassword(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newlogin", consumes = JSON, produces = JSON)
    public ResponseEntity setLogin(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Wrong password"));
        }
        if (!service.checkLogin(body.getFieldToChange())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Login already exsists"));
        }
        service.changeLogin(id, body.getFieldToChange());

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/newemail", consumes = JSON, produces = JSON)
    public ResponseEntity setEmail(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (httpSession.getAttribute(USER_ID) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User not authorize"));
        }
        final Long id = (Long) httpSession.getAttribute(USER_ID);
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Wrong password"));
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



