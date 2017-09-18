package application.models;

import application.utils.requests.SignupRequest;

import javax.validation.constraints.NotNull;

public class User {
    private final long id;
    private final String login;
    private final String password;
    private final String email;

    public User(long id, SignupRequest user) {
        this.id = id;
        this.password = user.getPassword();
        this.login = user.getLogin();
        this.email = user.getEmail();
    }

    public User(long id, String password, String login, String email) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.email = email;
    }

    @NotNull
    public long getId() {
        return id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getEmail() {
        return email;
    }
}
