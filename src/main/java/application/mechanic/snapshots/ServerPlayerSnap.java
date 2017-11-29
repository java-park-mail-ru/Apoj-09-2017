package application.mechanic.snapshots;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("MissortedModifiers")
public class ServerPlayerSnap {
    private long id = -1;
    @NotNull
    private String mode;
    @NotNull
    private String status;
    @Nullable
    private String role;
    @Nullable
    private byte[] song;

    public ServerPlayerSnap(long id, @NotNull String mode, @NotNull String status, @Nullable String role, @Nullable byte[] song) {
        this.id = id;
        this.mode = mode;
        this.status = status;
        this.role = role;
        this.song = song;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NotNull String status) {
        this.status = status;
    }

    @Nullable
    public byte[] getSong() {
        return song;
    }

    public void setSong(@Nullable byte[] song) {
        this.song = song;
    }
}
