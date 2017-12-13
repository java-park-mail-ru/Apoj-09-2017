package application.services;

import application.db.UserDao;
import application.models.User;
import application.utils.requests.SignupRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@SuppressWarnings("MissortedModifiers")
@Service
public class AccountService {
    @NotNull
    private final UserDao db;
    @NotNull
    private final PasswordEncoder encoder;

    public AccountService(@NotNull UserDao db, @NotNull PasswordEncoder encoder) {
        this.db = db;
        this.encoder = encoder;
    }

    @NotNull
    public Long addUser(@NotNull SignupRequest user) {
        final String encodedPassword = encoder.encode(user.getPassword());
        return db.addUser(user.getLogin(), encodedPassword, user.getEmail());
    }

    public void changePassword(@NotNull User user, @NotNull String password) {
        password = encoder.encode(password);
        user.setPassword(password);
        db.changeUserData(user);
    }

    public void changeLogin(@NotNull User user, @NotNull String login) {
        user.setLogin(login);
        db.changeUserData(user);
    }

    public void changeEmail(@NotNull User user, @NotNull String email) {
        user.setEmail(email);
        db.changeUserData(user);
    }

    @Nullable
    public User getUser(long id) {
        return db.getUser(id);
    }

    @Nullable
    public User getUser(@NotNull String login) {
        return db.getUser(login);
    }

    public boolean checkLogin(@NotNull String login) {
        return db.getIdByLogin(login) == null;
    }

    public boolean checkEmail(@NotNull String email) {
        return db.getIdByEmail(email) == null;
    }

    public boolean checkSignup(@NotNull String login, @NotNull String email) {
        return db.checkSignup(login, email);
    }

    public boolean checkSignin(long id, @NotNull String password) {
        final User user = db.getUser(id);
        return user != null && encoder.matches(password, user.getPassword());

    }

    public Integer updateSScore(long id, boolean result){
        return db.updateSScore(id, result);
    }

    public Integer updateMScore(long id, boolean result){
        return db.updateMScore(id, result);
    }

    public void clear() {
        db.clear();
    }

}
