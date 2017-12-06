package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("MissortedModifiers")
public class ServerSnap extends Message {
    @NotNull
    private String type;
    @NotNull
    private String data;

    public ServerSnap(@NotNull String type) {
        this.type = type;
    }

    @NotNull
    public String getData() {
        return data;
    }

    public void setData(@NotNull String data) {
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
