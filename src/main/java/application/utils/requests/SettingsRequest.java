package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class SettingsRequest {
    @NotNull
    private final String password;
    private final String newPassword;
    private final String login;
    private final String email;

    @JsonCreator
    public SettingsRequest(@JsonProperty("password") @NotNull String password,
                           @JsonProperty("newPassword")  String newPassword,
                           @JsonProperty("login")  String login,
                           @JsonProperty("email")  String email) {
        this.password = password;
        this.newPassword = newPassword;
        this.login = login;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public String getNewPassword() { return newPassword; }
}

