package application.utils;

import application.utils.requests.SignupRequest;
import application.utils.requests.SigninRequest;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {
    private static final String LOGIN_ERROR = "Invalid login ";
    private static final String EMAIL_ERROR = "Invalid email ";
    private static final String EMPTY_LOGIN = "Empty login ";
    private static final String SHORT_LOGIN = "Short login ";
    private static final String LONG_LOGIN = "Long login ";
    private static final String EMPTY_EMAIL = "Empty email ";
    private static final String EMPTY_PASSWORD = "Empty password ";
    private static final String SHORT_PASSWORD = "Short password ";
    private static final String LONG_PASSWORD = "Long password ";
    private static final int LOGIN_MIN_LENGTH = 3;
    private static final int LOGIN_MAX_LENGTH = 15;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 24;

    public static String checkLogin(@NotNull String login) {
        String error = "";
        if (StringUtils.isEmpty(login)) {
            error += EMPTY_LOGIN;
        }
        if (login.length() < LOGIN_MIN_LENGTH) {
            error += SHORT_LOGIN;
        }
        if (login.length() > LOGIN_MAX_LENGTH) {
            error += LONG_LOGIN;
        }
        final Pattern p = Pattern.compile("^[a-z0-9_-]{3,15}$");
        final Matcher m = p.matcher(login);
        if (!m.matches()) {
            error += LOGIN_ERROR;
        }
        return error;
    }

    public static String checkEmail(@NotNull String email) {
        String error = "";
        if (StringUtils.isEmpty(email)) {
            error += EMPTY_EMAIL;
        }
        final Pattern p = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
        final Matcher m = p.matcher(email);
        if (!m.matches()) {
            error += EMAIL_ERROR;
        }
        return error;
    }

    public static String checkPassword(@NotNull String password) {
        String error = "";
        if (StringUtils.isEmpty(password)) {
            error += EMPTY_PASSWORD;
        }
        if (password.length() < PASSWORD_MIN_LENGTH) {
            error += SHORT_PASSWORD;
        }
        if (password.length() > PASSWORD_MAX_LENGTH) {
            error += LONG_PASSWORD;
        }
        return error;
    }

    public static String checkSignup(@NotNull SignupRequest user) {
        return checkLogin(user.getLogin()) + checkPassword(user.getPassword()) + checkEmail(user.getEmail());
    }

    public static String checkSignin(@NotNull SigninRequest user) {
        return checkLogin(user.getLogin()) + checkPassword(user.getPassword());
    }

}
