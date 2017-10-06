package application.utils.responses;

import application.models.User;
import application.utils.requests.SignupRequest;

import javax.validation.constraints.NotNull;

public class UserResponseWP {
    private final long id;
    @NotNull
    private final String login;
    @NotNull
    private final String email;

    public UserResponseWP(@NotNull User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
    }

    public UserResponseWP(Long id, SignupRequest user) {
        this.id = id;
        this.login = user.getLogin();
        this.email = user.getEmail();
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
