package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public String getData() {
        return data;
    }

}
