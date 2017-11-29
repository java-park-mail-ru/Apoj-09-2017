package application.mechanic.avatar;

import application.models.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("MissortedModifiers")
public class Player {
    @NotNull
    private final User user;
    @NotNull
    private String status = "";
    @NotNull
    private String role = "";

    public Player(@NotNull User user) {
        this.user = user;
    }

    public @NotNull User getUser() {
        return user;
    }

    public long getId() {
        return user.getId();
    }

    public @NotNull String getStatus() {
        return status;
    }

    public void setStatus(@NotNull String status) {
        this.status = status;
    }

    @NotNull
    public String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
        this.role = role;
    }
}
