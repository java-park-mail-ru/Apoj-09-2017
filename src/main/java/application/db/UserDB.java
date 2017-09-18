package application.db;

import application.models.User;
import application.utils.requests.SignupRequest;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.HashMap;


public  class UserDB{
  private Map<Long, User> map = new HashMap<>();
  private static long id = 1;
  @Autowired
  private PasswordEncoder passwordEncoder;
  public UserDB() {}

  @Bean
  public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
  }
  @NotNull
  public long addUser(SignupRequest user) {
      map.put(id, new User(id, user));
      return id++;
  }

  public long changeUserData(User user){
      map.put(user.getId(), user);
      return user.getId();
  }

  public User getUser(long id) {
      return map.get(id);
  }

  @Nullable
  public Long getId(String login) {
      for(long i = 1; i < id; ++i) {
          if (map.get(i).getLogin().equals(login)) {
              return i;
          }
      }
      return null;
  }

  public boolean hasLogin(String login) {
      for(long i = 1; i < id; ++i) {
          if (map.get(i).getLogin().equals(login)) {
              return true;
          }
      }
      return false;
  }

  public boolean hasEmail(String email) {
      for(long i = 1; i < id; ++i) {
          if (map.get(i).getEmail().equals(email)) {
              return true;
          }
      }
      return false;
    }

    public boolean checkSignin(long id, String password) {
      return passwordEncoder.matches(password,map.get(id).getPassword());
  }
}
