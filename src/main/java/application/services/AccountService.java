package application.services;

import application.db.UserDB;

import application.models.User;
import application.utils.requests.SignupRequest;
import org.springframework.stereotype.Service;
import org.jetbrains.annotations.Nullable;


@Service
public class AccountService {
    private static UserDB db;

    public AccountService(UserDB newDB){
        db = newDB;
    }

    public long addUser(SignupRequest user){
        return db.addUser(user);
    }

    public long changeUserData(User user) { return db.changeUserData(user);}

    public User getUser(long id){
        return db.getUser(id);
    }

    @Nullable
    public Long getId(String login){
        return db.getId(login);
    }

    public boolean checkSignup(String login, String email){
        return !(db.hasLogin(login) || db.hasEmail(email));
    }

    public boolean checkSignin(long id, String password){
        return db.checkSignin(id, password);
    }

}
