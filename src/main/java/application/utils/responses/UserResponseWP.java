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
    private final int sscore;
    private final int mscore;

    public UserResponseWP(@NotNull User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.sscore = user.getSscore();
        this.mscore = user.getMscore();
    }

    public UserResponseWP(Long id, SignupRequest user) {
        this.id = id;
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.sscore = 1000;
        this.mscore = 1000;
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

    public int getSscore() {
        return sscore;
    }

    public int getMscore() {
        return mscore;
    }
}
