package application.db;

import application.models.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface UserDao {
    @Nullable
    Long addUser(String login, String password, String email);

    void changeUserData(User user);

    @Nullable
    User getUser(long userId);

    @Nullable
    User getUser(String login);

    @Nullable Long getIdByLogin(@NotNull String login);

    @Nullable Long getIdByEmail(@NotNull String email);

    boolean checkSignup(@NotNull String login, @NotNull String email);

    void clear();
}
