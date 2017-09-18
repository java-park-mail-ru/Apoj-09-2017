package application.models;

import application.utils.requests.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;

public class User{
    private final long id;
    private final String login;
    private final String password;
    private final String email;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    public User(long id, SignupRequest user) {
        this.id = id;
        this.password = passwordEncoder.encode(user.getPassword());
        this.login = user.getLogin();
        this.email = user.getEmail();
    }

    public User(long id, String password, String login, String email){
        this.id = id;
        this.password = passwordEncoder.encode(password);
        this.login = login;
        this.email = email;
    }

    @NotNull
    public long getId() { return id; }

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
