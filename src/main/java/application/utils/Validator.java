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
    private static final String PASSWORD_ERROR = "Invalid password ";

    public static boolean checkLogin(@NotNull String login) {
        if (StringUtils.isEmpty(login)) {
            return false;
        }
        final Pattern p = Pattern.compile("^[a-z0-9_-]{3,15}$");
        final Matcher m = p.matcher(login);
        return m.matches();
    }

    public static boolean checkEmail(@NotNull String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        final Pattern p = Pattern.compile("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$");
        final Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean checkPassword(@NotNull String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        final Pattern p = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        final Matcher m = p.matcher(password);
        return m.matches();
    }

    public static String checkSignup(@NotNull SignupRequest user) {
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

    public static String checkSignin(@NotNull SigninRequest user) {
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
