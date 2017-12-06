package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("MissortedModifiers")
public class ServerSnap extends Message {
    @NotNull
    private String type;
    @Nullable
    private byte[] data;

    public ServerSnap(@NotNull String type) {
        this.type = type;
    }

    @Nullable
    public byte[] getData() {
        return data;
    }

    public void setData(@Nullable byte[] data) {
        this.data = data;
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }
}
