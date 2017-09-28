package application.db;

import application.models.User;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;


public class UserDB {
    private Map<Long, User> map = new HashMap<>();
    private static AtomicLong id = new AtomicLong(1);

    @NotNull
    public long addUser(String login, String password, String email) {
        final long newId = id.get();
        map.put(newId, new User(newId, login, password, email));
        return id.getAndIncrement();
    }

    public void changeUserData(User user) {
        map.put(user.getId(), user);
    }

    @NotNull
    public User getUser(long userId) {
        return map.get(userId);
    }

    @Nullable
    public Long getId(String login) {
        for (User user : map.values()) {
            if (user.getLogin().equals(login)) {
                return user.getId();
            }
        }
        return null;
    }

    public boolean hasLogin(String login) {
        for (User user : map.values()) {
            if (user.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEmail(String email) {
        for (User user : map.values()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
