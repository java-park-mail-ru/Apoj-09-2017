package application.utils.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class SigninRequest{

  @NotNull
  private String login;
  @NotNull
  private String password;

  @JsonCreator
  public SigninRequest(@JsonProperty("login") @NotNull String login,
                       @JsonProperty("password") @NotNull String password) {
    this.login = login;
    this.password = password;
  }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

}
