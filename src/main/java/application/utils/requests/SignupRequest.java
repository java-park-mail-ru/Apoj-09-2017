package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class SignupRequest {
    @NotNull
    private final String login;
    @NotNull
    private final String password;
    @NotNull
    private final String email;

    @JsonCreator
    public SignupRequest(@JsonProperty("login") @NotNull String login,
                         @JsonProperty("password") @NotNull String password,
                         @JsonProperty("email") @NotNull String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }
}
