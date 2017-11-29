package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("MissortedModifiers")
public class ClientSnap extends Message{
    private long id = -1;
    @NotNull
    private String mode;
    @NotNull
    private String status;
    @Nullable
    private String role;
    @Nullable
    private byte[] song;

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

    public @NotNull String getMode() {
        return mode;
    }

    public void setMode(@NotNull String mode) {
        this.mode = mode;
    }
}
