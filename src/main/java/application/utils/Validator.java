package application.utils;

import application.utils.requests.SignupRequest;
import application.utils.requests.SigninRequest;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {
    private static final String LOGIN_ERROR = "Invalid login";
    private static final String EMAIL_ERROR = "Invalid email";
    private static final String EMPTY_LOGIN = "Empty login";
    private static final String SHORT_LOGIN = "Short login";
    private static final String LONG_LOGIN = "Long login";
    private static final String EMPTY_EMAIL = "Empty email";
    private static final String EMPTY_PASSWORD = "Empty password";
    private static final String SHORT_PASSWORD = "Short password";
    private static final String LONG_PASSWORD = "Long password";
    private static final int LOGIN_MIN_LENGTH = 3;
    private static final int LOGIN_MAX_LENGTH = 15;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 24;

    public static ArrayList<String> checkLogin(@NotNull String login) {
        final ArrayList<String> error = new ArrayList<>();
        if (StringUtils.isEmpty(login)) {
            error.add(EMPTY_LOGIN);
        }
        final int length = login.length();
        if (length < LOGIN_MIN_LENGTH) {
            error.add(SHORT_LOGIN);
        }
        if (length > LOGIN_MAX_LENGTH) {
            error.add(LONG_LOGIN);
        }
        final Pattern p = Pattern.compile("^[a-z0-9_-]{3,15}$");
        final Matcher m = p.matcher(login);
        if (!m.matches()) {
            error.add(LOGIN_ERROR);
        }
        return error;
    }

    public static ArrayList<String> checkEmail(@NotNull String email) {
        final ArrayList<String> error = new ArrayList<>();
        if (StringUtils.isEmpty(email)) {
            error.add(EMPTY_EMAIL);
        }
        final Pattern p = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
        final Matcher m = p.matcher(email);
        if (!m.matches()) {
            error.add(EMAIL_ERROR);
        }
        return error;
    }

    public static ArrayList<String> checkPassword(@NotNull String password) {
        final ArrayList<String> error = new ArrayList<>();
        if (StringUtils.isEmpty(password)) {
            error.add(EMPTY_PASSWORD);
        }
        final int length = password.length();
        if (length < PASSWORD_MIN_LENGTH) {
            error.add(SHORT_PASSWORD);
        }
        if (length > PASSWORD_MAX_LENGTH) {
            error.add(LONG_PASSWORD);
        }
        return error;
    }

    public static ArrayList<String> checkSignup(@NotNull SignupRequest user) {
        final ArrayList<String> error = new ArrayList<>();
        error.addAll(checkLogin(user.getLogin()));
        error.addAll(checkPassword(user.getPassword()));
        error.addAll(checkEmail(user.getEmail()));
        return error;
    }

    public static ArrayList<String> checkSignin(@NotNull SigninRequest user) {
        final ArrayList<String> error = new ArrayList<>();
        error.addAll(checkLogin(user.getLogin()));
        error.addAll(checkPassword(user.getPassword()));
        return error;
    }

}
