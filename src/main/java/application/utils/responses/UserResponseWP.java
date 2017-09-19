package application.utils.responses;

import application.models.User;

import javax.validation.constraints.NotNull;

public class UserResponseWP {
    @NotNull
    private final long id;
    @NotNull
    private String login;
    @NotNull
    private String email;

    public UserResponseWP(@NotNull User user) {
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

    public long getId() {
        return id;
    }
}
