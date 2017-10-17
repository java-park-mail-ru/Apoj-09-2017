package application.db;

import application.models.User;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;


public interface UserDao {
    long addUser(String login, String password, String email);

    void changeUserData(User user);

    @NotNull
    User getUser(long userId);

    @Nullable Long getId(@Nullable String login, @Nullable String email);

    boolean hasId(long id);
}
