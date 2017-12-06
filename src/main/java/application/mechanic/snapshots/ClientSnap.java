package application.mechanic.snapshots;

import application.websocket.Message;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("MissortedModifiers")
public class ClientSnap extends Message {
    @NotNull
    private String type;
    private String data;
    @Nullable
    private String answer;

    public ClientSnap(@NotNull String type, String data, @Nullable JsonNode answer) {
        this.type = type;
        this.data = data;
        if(answer != null) {
            this.answer = answer.asText();
        }
    }

    @NotNull
    public String getType() {
        return type;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    @Nullable
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(@Nullable String answer) {
        this.answer = answer;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
