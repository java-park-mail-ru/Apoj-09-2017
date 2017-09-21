package application.services;

import application.db.UserDB;
import application.models.User;
import application.utils.requests.SignupRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    private final UserDB db;
    private final PasswordEncoder encoder;

    public AccountService(@NotNull UserDB db, @NotNull PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    public Long addUser(SignupRequest user) {
        final String encodedPassword = encoder.encode(user.getPassword());
        return db.addUser(user.getLogin(), encodedPassword, user.getEmail());
    }

    public void changePassword(long id, String password) {
        password = encoder.encode(password);
        final User user = db.getUser(id);
        user.setPassword(password);
        db.changeUserData(user);
    }

    public void changeLogin(long id, String login) {
        final User user = db.getUser(id);
        user.setLogin(login);
        db.changeUserData(user);
    }

    public void changeEmail(long id, String email) {
        final User user = db.getUser(id);
        user.setEmail(email);
        db.changeUserData(user);
    }

    @NotNull
    public User getUser(long id) {
        return db.getUser(id);
    }

    @Nullable
    public Long getId(String login) {
        return db.getId(login);
    }

    public boolean checkLogin(String login) {
        return !db.hasLogin(login);
    }

    public boolean checkEmail(String login) {
        return !db.hasEmail(login);
    }

    public boolean checkSignup(String login, String email) {
        return !(db.hasLogin(login) || db.hasEmail(email));
    }

    public boolean checkSignin(long id, String password) {
        return encoder.matches(password, db.getUser(id).getPassword());
    }

}
