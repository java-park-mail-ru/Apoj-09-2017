package application.utils.responses;

import application.models.User;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

public class UserResponseWP {
    @NotNull
    private final Long id;
    @NotNull
    private final String login;
    @NotNull
    private final String email;

    public UserResponseWP(@Nullable @NotNull User user) {
        this.id = user.getId();
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
