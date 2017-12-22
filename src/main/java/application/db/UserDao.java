package application.db;

import application.models.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public interface UserDao {
    int DEFAULT_SCORE = 1000;
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

    @NotNull
    Integer updateSScore(long userId, boolean result);

    @NotNull
    Integer updateMScore(long userId, boolean result);

    List<User> getSTop(@NotNull Integer limit, @NotNull Integer since);

    List<User> getMTop(@NotNull Integer limit, @NotNull Integer since);

    void clear();
}
