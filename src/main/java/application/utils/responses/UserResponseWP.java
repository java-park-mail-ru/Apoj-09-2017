package application.utils.responses;

import application.models.User;

public class UserResponseWP {
    private final long id;
    private String login;
    private String email;

    public UserResponseWP(User user) {
    	this.id = user.getId();
    	this.login = user.getLogin();
    	this.email = user.getEmail();
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
