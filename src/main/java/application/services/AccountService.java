package application.services;

import application.db.UserDao;
import application.models.User;
import application.utils.requests.SignupRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    private final UserDao db;
    private final PasswordEncoder encoder;

    public AccountService(@NotNull UserDao db, @NotNull PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    public Long addUser(SignupRequest user) {
        final String encodedPassword = encoder.encode(user.getPassword());
        return db.addUser(user.getLogin(), encodedPassword, user.getEmail());
    }

    public void changePassword(User user, String password) {
        password = encoder.encode(password);
        user.setPassword(password);
        db.changeUserData(user);
    }

    public void changeLogin(User user, String login) {
        user.setLogin(login);
        db.changeUserData(user);
    }

    public void changeEmail(User user, String email) {
        user.setEmail(email);
        db.changeUserData(user);
    }

    @Nullable
    public User getUser(long id) {
        return db.getUser(id);
    }

    @Nullable
    public User getUser(String login) {
        return db.getUser(login);
    }

    @Nullable
    public Long getId(String login) {
        return db.getIdByLogin(login);
    }

    public boolean checkLogin(String login) {
        return db.getIdByLogin(login) == null;
    }

    public boolean checkEmail(String email) {
        return db.getIdByEmail(email) == null;
    }

    public boolean checkSignup(String login, String email) {
        return db.checkSignup(login, email);
    }

    public boolean checkSignin(long id, String password) {
        final User user = db.getUser(id);
        return encoder.matches(password, user.getPassword());
    }

    public void clear() {
        db.clear();
    }

}
