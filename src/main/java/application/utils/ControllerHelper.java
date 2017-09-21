package application.utils;

import application.services.AccountService;
import application.utils.requests.SettingsRequest;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import application.utils.responses.MessageResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;


public class ControllerHelper {
    private static final String USER_ID = "userId";

    @Nullable
    public ResponseEntity signupCheck(SignupRequest body, HttpSession httpSession, AccountService service) {
        final String error = Validator.checkSignup(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("User already authorize"));
        }

        if (!service.checkSignup(body.getLogin(), body.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Login or Email already exists"));
        }
        return null;
    }

    @Nullable
    public ResponseEntity signinCheck(SigninRequest body, HttpSession httpSession, AccountService service, @Nullable Long id) {
        final String error = Validator.checkSignin(body);
        if (!error.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(error));
        } else if (httpSession.getAttribute(USER_ID) != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("User already authorize"));
        }
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));
        }

        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Wrong login or password"));
        }
        return null;
    }

    @Nullable
    public ResponseEntity newCheck(SettingsRequest body, HttpSession httpSession, AccountService service, Long id) {
        if (httpSession.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User not authorize"));
        }
        if (!service.checkSignin(id, body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Wrong password"));
        }
        return null;
    }
}
