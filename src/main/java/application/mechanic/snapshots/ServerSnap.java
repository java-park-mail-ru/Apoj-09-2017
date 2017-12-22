package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

public class ServerSnap extends Message {
    @NotNull
    private String type;
    @NotNull
    private String data;

    public ServerSnap(@NotNull String type, @NotNull String data) {
        this.type = type;
        this.data = data;
    }

    @NotNull
    public String getData() {
        return data;
    }

    @NotNull
    public String getType() {
        return type;
    }
}
