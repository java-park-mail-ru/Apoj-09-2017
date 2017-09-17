package application.db;

import application.models.User;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;

import java.util.Map;
import java.util.HashMap;


public  class UserDB{
  private Map<Long, User> map = new HashMap<>();
  private static long id = 0;

  public UserDB() {}

  public User addUser(SignupRequest user) {
      map.put(id, new User(id, user));
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
                  if(map.get(i).getPassword().equals(user.getPassword())) {
                      return true;
                  }
              }
          }
      return false;
  }
}
