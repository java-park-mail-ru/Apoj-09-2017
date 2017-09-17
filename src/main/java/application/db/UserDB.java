package application.db;

import application.models.User;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;
import org.jetbrains.annotations.Nullable;


import java.util.Map;
import java.util.HashMap;


public  class UserDB{
  private Map<Long, User> map = new HashMap<>();
  private static long id = 1;

  public UserDB() {}

  public long addUser(SignupRequest user) {
      map.put(id, new User(id, user));
      return id++;
  }

  public User getUser(long id) {
      return map.get(id);
  }

  public @Nullable Long getId(String login) {
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
          if (map.get(i).getLogin().equals(email)) {
              return true;
          }
      }
      return false;
    }


  public boolean checkSignin(long id, String password) {
      return map.get(id).getPassword().equals(password);
  }
}
