package application.utils;

import application.utils.requests.SignupRequest;
import application.utils.requests.SigninRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {
    public static final String LOGIN_ERROR = "Invalid login ";
    public static final String EMAIL_ERROR = "Invalid email ";
    public static final String PASSWORD_ERROR = "Invalid password ";

    public static boolean checkLogin(String login) {
        final Pattern p = Pattern.compile("^[a-z0-9_-]{3,15}$");
        final Matcher m = p.matcher(login);
        return m.matches();
    }

    public static boolean checkEmail(String email) {
        final Pattern p = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
        final Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPassword(String password) {
        final Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,20}$");
        final Matcher m = p.matcher(password);
        return m.matches();
    }

    public static String checkSignup(SignupRequest user) {
        String error = "";
        if (!checkLogin(user.getLogin())) {
            error += LOGIN_ERROR;
        }

        if (!checkEmail(user.getEmail())) {
            error += EMAIL_ERROR;
        }

        if (!checkPassword(user.getPassword())) {
            error += PASSWORD_ERROR;
        }

        return error;
    }

    public static String checkSignin(SigninRequest user) {
        String error = "";
        if (!checkLogin(user.getLogin())) {
            error += LOGIN_ERROR;
        }

        if (!checkPassword(user.getPassword())) {
            error += PASSWORD_ERROR;
        }

        return error;

    }

}
