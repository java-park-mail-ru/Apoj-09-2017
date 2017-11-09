package application.db;

import application.models.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface UserDao {
    @NotNull
    Long addUser(@NotNull String login, @NotNull String password, @NotNull String email);

    void changeUserData(@NotNull User user);

    @Nullable
    User getUser(long userId);

    @Nullable
    User getUser(@NotNull String login);

    @Nullable
    Long getIdByLogin(@NotNull String login);

    @Nullable
    Long getIdByEmail(@NotNull String email);

    boolean checkSignup(@NotNull String login, @NotNull String email);

    void clear();
}
