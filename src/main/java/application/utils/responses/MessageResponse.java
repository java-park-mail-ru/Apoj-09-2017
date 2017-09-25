package application.utils.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class MessageResponse {
    @NotNull
    private final String message;

    public MessageResponse(@JsonProperty("error") @NotNull String message) {
        this.message = message;
    }

    @NotNull
    public String getMessage() {
        return message;
    }
}
