package application.utils.responses;

import application.models.User;

import javax.validation.constraints.NotNull;

public class UserResponseWP {
    private final long id;
    private String login;
    private String email;

    public UserResponseWP(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
    }

}
