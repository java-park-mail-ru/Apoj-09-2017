package application.db;

import application.models.User;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.HashMap;


public  class UserDB{
  private Map<Long, User> map = new HashMap<>();
  private static long id = 0;
  @Autowired
  private PasswordEncoder passwordEncoder;
  public UserDB() {}

  @Bean
  public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
  }

  public User addUser(SignupRequest user) {
      User userModel = new User(id, user);
      userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

      map.put(id, userModel);
      return map.get(id++);
  }

  public User getUser(String login) {
      for (long i = 0; i < id; ++i) {
          if (map.get(i).getLogin().equals(login)) {
              return map.get(i);
          }
      }
      return null;
  }

  public boolean hasLogin(String login) {
          for(long i = 0; i < id; ++i) {
              if (map.get(i).getLogin().equals(login)) {
                  return true;
              }
          }
      return false;
  }

  public boolean hasUser(SigninRequest user) {
          for(long i = 0; i < id; ++i) {
              if (map.get(i).getLogin().equals(user.getLogin())) {
                  if(passwordEncoder.matches(user.getPassword(),map.get(i).getPassword())) {
                      return true;
                  }
              }
          }
      return false;
  }
}
