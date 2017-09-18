package application.db;

import application.models.User;
import application.utils.requests.SignupRequest;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.HashMap;


public class UserDB {
    private Map<Long, User> map = new HashMap<>();
    private static long id = 1;

    @NotNull
    public long addUser(SignupRequest user) {
        map.put(id, new User(id, user));
        return id++;
    }

    public void changeUserData(User user) {
        map.put(user.getId(), user);
    }

    public User getUser(long userId) {
        return map.get(userId);
    }

    public @Nullable Long getId(String login) {
        for (long i = 1; i < id; ++i) {
            if (map.get(i).getLogin().equals(login)) {
                return i;
            }
        }
        return null;
    }

    public boolean hasLogin(String login) {
        for (long i = 1; i < id; ++i) {
            if (map.get(i).getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEmail(String email) {
        for (long i = 1; i < id; ++i) {
            if (map.get(i).getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
