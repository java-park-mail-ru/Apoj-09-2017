package application.mechanic.avatar;

import application.mechanic.snapshots.ServerPlayerSnap;
import application.models.User;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("MissortedModifiers")
public class Player {
    @NotNull
    private final User user;
    @NotNull
    private String mode;
    @NotNull
    private String status = "";
    @NotNull
    private String role = "";

//    public Player(@NotNull User user, @NotNull String mode, @NotNull String status) {
//        this.user = user;
//        this.mode = mode;
//        this.status = status;
//    }

    public Player(@NotNull User user, @NotNull String mode) {
        this.user = user;
        this.mode = mode;
    }

    @NotNull
    public ServerPlayerSnap generateSnap() {
        return new ServerPlayerSnap(getId(), mode, status, role, null);
    }

    public @NotNull User getUser() {
        return user;
    }

    public long getId() {
        return user.getId();
    }

    public @NotNull String getMode() {
        return mode;
    }

    public @NotNull String getStatus() {
        return status;
    }

    public void setMode(@NotNull String mode) {
        this.mode = mode;
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
