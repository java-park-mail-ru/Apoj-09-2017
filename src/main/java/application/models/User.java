package application.models;

import application.utils.requests.SignupRequest;

public class User{
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

    public long getId() { return id; }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
