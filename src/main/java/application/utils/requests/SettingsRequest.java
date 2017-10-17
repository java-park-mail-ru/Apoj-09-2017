package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class SettingsRequest {

    private final @NotNull String password;
    private final @NotNull String fieldToChange;

    @JsonCreator
    public SettingsRequest(@JsonProperty("password") @NotNull String password,
                           @JsonProperty("change") @NotNull String fieldToChange) {
        this.password = password;
        this.fieldToChange = fieldToChange;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getFieldToChange() {
        return fieldToChange;
    }
}

