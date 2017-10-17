package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public class SignupRequest {

    private final @NotNull String login;
    private final @NotNull String password;
    private final @NotNull String email;

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
