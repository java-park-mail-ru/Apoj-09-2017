package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("MissortedModifiers")
public class ClientSnap extends Message{
    @NotNull
    private String type;
    private byte[] data;
    private String answer;

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
