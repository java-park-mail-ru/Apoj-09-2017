package application.mechanic.avatar;

import application.models.User;
import org.jetbrains.annotations.NotNull;

public class Player {
    @NotNull
    private final User user;
    @NotNull
    private String role;

    public Player(@NotNull User user, @NotNull String role) {
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
    public String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
        this.role = role;
    }
}
