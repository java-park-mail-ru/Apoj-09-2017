package application.mechanic.avatar;

import application.mechanic.Config;
import application.models.User;
import org.jetbrains.annotations.NotNull;

public class Player {
    @NotNull
    private final User user;
    @NotNull
    private Config.Role role;

    public Player(@NotNull User user, @NotNull Config.Role role) {
        this.user = user;
        this.role = role;
    }

    @NotNull
    public User getUser() {
        return user;
    }

    public long getId() {
        return user.getId();
    }

    @NotNull
    public Config.Role getRole() {
        return role;
    }

    public void setRole(@NotNull Config.Role role) {
        this.role = role;
    }
}
