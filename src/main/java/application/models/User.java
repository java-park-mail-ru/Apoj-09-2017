package application.models;

import application.utils.requests.SignupRequest;

import javax.validation.constraints.NotNull;

public class User {
    private final long id;
    private String login;
    private String password;
    private String email;

    public User(long id, SignupRequest user) {
        this.id = id;
        this.password = user.getPassword();
        this.login = user.getLogin();
        this.email = user.getEmail();
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

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
