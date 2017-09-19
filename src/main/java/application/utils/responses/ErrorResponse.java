package application.utils.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ErrorResponse {
    @NotNull
    private final String message;

    public ErrorResponse(@JsonProperty("error") @NotNull String message) {
        this.message = message;
    }

    @NotNull
    public String getMessage() {
        return message;
    }
}
