package application.controllers;

import application.db.UserDB;
import application.models.User;
import application.services.AccountService;
import application.utils.Validator;
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (!service.checkSignup(body.getLogin(), body.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
        }

        Long id = service.getId(body.getLogin());
        if (id == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(2);
        }

        if (!service.checkSignin(id, body.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(3);
        }

        httpSession.setAttribute("userId", id);
        return ResponseEntity.ok(new UserResponseWP(service.getUser(id)));
    }
}
