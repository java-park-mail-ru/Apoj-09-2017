package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("MissortedModifiers")
public class ClientSnap extends Message {
    @NotNull
    private String type;
    @NotNull
    private String data;

    public ClientSnap(@NotNull String type, @NotNull String data) {
        this.type = type;
        this.data = data;
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    @NotNull
    public String getData() {
        return data;
    }

    public void setData(@NotNull String data) {
        this.data = data;
    }
}
