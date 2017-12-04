package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("MissortedModifiers")
public class SingleServerSnap extends Message {
    @NotNull
    private String status;
    @Nullable
    private byte[] data;

    public SingleServerSnap(@NotNull String status) {
        this.status = status;
    }

    @Nullable
    public byte[] getData() {
        return data;
    }

    public void setData(@Nullable byte[] data) {
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
