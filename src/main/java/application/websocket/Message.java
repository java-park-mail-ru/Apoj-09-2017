package application.websocket;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Message {
    @Nullable
    private String type;
    @Nullable
    private String data;

    public Message() {
    }

    @JsonCreator
    public Message(@JsonProperty("type") @NotNull String type,
                   @JsonProperty("data") @NotNull String data) {
        this.type = type;
        this.data = data;
    }

    public Message(@NotNull Class clazz, @NotNull String data) {
        this(clazz.getName(), data);
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public String getData() {
        return data;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public void setData(@NotNull String data) {
        this.data = data;
    }
}
