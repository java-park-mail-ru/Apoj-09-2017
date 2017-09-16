package application.controllers;

import application.db.UserDB;
import application.models.User;
import application.utils.Validator;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import application.utils.responses.UserResponseWP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class SessionController {
    private static UserDB db = new UserDB();

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity signup(@RequestBody SignupRequest body) {
        if (!Validator.checkSignup(body, db)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User newUser = db.addUser(body);
        return ResponseEntity.ok(new UserResponseWP(newUser));
    }

    @PostMapping(path = "/signin", consumes = "application/json", produces = "application/json")
    public ResponseEntity greetingSubmit(@RequestBody SigninRequest body) {
        if (!Validator.checkSignin(body, db)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User authorizedUser = db.getUser(body.getLogin());
        return ResponseEntity.ok(new UserResponseWP(authorizedUser));
    }
}
