package application.db;

import application.models.User;

import org.jetbrains.annotations.Nullable;


public interface UserDao {
    long addUser(String login, String password, String email);

    void changeUserData(User user);

    @Nullable
    User getUser(long userId);

    @Nullable
    User getUser(String login);

    @Nullable Long getId(@Nullable String login, @Nullable String email);
}
