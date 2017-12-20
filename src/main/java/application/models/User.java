package application.models;

import javax.validation.constraints.NotNull;

public class User {
    private final Long id;
    private String login;
    private String password;
    private String email;
    private int sscore;
    private int mscore;

    public User(Long id, String login, String password, String email) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.email = email;
    }

    public User(Long id, String login, String password, String email, int sscore, int mscore) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.email = email;
        this.sscore = sscore;
        this.mscore = mscore;
    }

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

    public int getSscore() {
        return sscore;
    }

    public void setSscore(int sscore) {
        this.sscore = sscore;
    }

    public int getMscore() {
        return mscore;
    }

    public void setMscore(int mscore) {
        this.mscore = mscore;
    }
}
