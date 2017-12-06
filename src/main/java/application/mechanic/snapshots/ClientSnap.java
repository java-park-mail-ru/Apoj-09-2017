package application.mechanic.snapshots;

import application.websocket.Message;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("MissortedModifiers")
public class ClientSnap extends Message {
    @NotNull
    private String type;
    private String data;
    private String answer;

    public ClientSnap(@NotNull String type, String data, String answer) {
        this.type = type;
        this.data = data;
        this.answer = answer;
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
