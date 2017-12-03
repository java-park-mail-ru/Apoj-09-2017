package application.mechanic.snapshots;

import application.mechanic.avatar.Player;
import application.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("MissortedModifiers")
public class MultiServerSnap extends Message{
    @NotNull
    private Player opponent;
    @NotNull
    private String status;
    @Nullable
    private byte[] data;

    public MultiServerSnap(@NotNull Player opponent, @NotNull String status) {
        this.opponent = opponent;
        this.status = status;
    }

    @NotNull
    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(@NotNull Player opponent) {
        this.opponent = opponent;
    }

    @Nullable
    public byte[] getData() {
        return data;
    }

    public void setData(@NotNull byte[] data) {
        this.data = data;
    }

    @NotNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NotNull String status) {
        this.status = status;
    }
}
