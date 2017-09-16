package application.db;

import application.models.User;
import application.utils.requests.SigninRequest;
import application.utils.requests.SignupRequest;


public  class UserDB{
  private User[] map = new User[100];
  private static int id = 0;

  public UserDB() {}

  public User addUser(SignupRequest user) {
      this.map[id] = new User(id, user);
      return map[id++];
  }
  public User getUser(String login) {
      for (int i = 0; i < id; ++i) {
          if (map[i].getLogin().equals(login)) {
              return map[i];
          }
      }
      return null;
  }

  public boolean hasLogin(String login) {
      if(id != 0) {
          for(int i = 0; i < id; ++i) {
              if (map[i].getLogin().equals(login)) {
                  return true;
              }
          }
      }
      return false;
  }

  public boolean hasUser(SigninRequest user) {
      if(id != 0) {
          for(int i = 0; i < id; ++i) {
              if (map[i].getLogin().equals(user.getLogin())) {
                  if(map[i].getPassword().equals(user.getPassword())) {
                      return true;
                  }
              }
          }
      }
      return false;
  }
}
