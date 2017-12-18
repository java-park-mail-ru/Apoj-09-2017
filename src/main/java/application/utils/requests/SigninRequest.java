package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jetbrains.annotations.NotNull;

public class SigninRequest {

    @NotNull
    private final String login;
    @NotNull
    private final String password;

    @JsonCreator
    public SigninRequest(@JsonProperty("login") @NotNull String login,
                         @JsonProperty("password") @NotNull String password) {
        this.login = login;
        this.password = password;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

}
