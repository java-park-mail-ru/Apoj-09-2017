package application.controllers;

import application.db.UserDB;
import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
import application.utils.requests.SettingsRequest;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import application.utils.responses.UserResponseWP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpSession;


@RestController
public class SessionController {
    private static UserDB db = new UserDB();
    private static AccountService service = new AccountService(db);

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody SignupRequest body, HttpSession httpSession) {
        if (!Validator.checkSignup(body)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(1);
        }else if (httpSession.getAttribute("userId") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(2);
        }

        if (!service.checkSignup(body.getLogin(), body.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(3);
        }

        long id = service.addUser(body);
        httpSession.setAttribute("userId", id);
        User newUser = new User(id, body);
        return ResponseEntity.ok(new UserResponseWP(newUser));
    }

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity greetingSubmit(@RequestBody SigninRequest body, HttpSession httpSession) {
        if (!Validator.checkSignin(body)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(1);
        }else if (httpSession.getAttribute("userId") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(2);
        }

        Long id = service.getId(body.getLogin());
        if (id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(3);
        }

        if (!service.checkSignin(id, body.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(4);
        }

        httpSession.setAttribute("userId", id);
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/settings", consumes = "application/json", produces = "application/json")
    public ResponseEntity settings(@RequestBody SettingsRequest body, HttpSession httpSession) {
        if (!Validator.checkSettings(body)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(1);
        }else if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(2);
        }

        Long id = (Long)httpSession.getAttribute("userId");

        if (!service.checkSignin(id, body.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(4);
        }

        User user = service.getUser(id);
        String email;
        String login;
        String newPassword;
        if(body.getEmail() != null && body.getEmail().trim().length()!=0)
            email = body.getEmail();
        else
            email = user.getEmail();
        if(body.getLogin() != null && body.getLogin().trim().length()!=0)
            login = body.getLogin();
        else
            login = user.getLogin();
        if(body.getNewPassword() != null && body.getNewPassword().trim().length()!=0)
            newPassword = body.getNewPassword();// ENCODE PASSWORD
        else
            newPassword = user.getPassword();
        User changed = new User (id, newPassword, login, email);
        service.changeUserData(changed);

        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }

    @PostMapping(path = "/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity logout( HttpSession httpSession) {
        if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(2);
        }
        httpSession.setAttribute("userId", null);
        return ResponseEntity.status(HttpStatus.OK).body(3);
    }

    @GetMapping(path = "/user", consumes = "application/json", produces ="application/json")
    public ResponseEntity user(HttpSession httpSession){
        if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(2);
        }
        Long id = (Long)httpSession.getAttribute("userId");
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }
}



