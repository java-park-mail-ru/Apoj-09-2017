package application.utils;

import application.utils.requests.SettingsRequest;
import application.utils.requests.SignupRequest;
import application.utils.requests.SigninRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator{

        private static boolean checkLogin(String login){
                Pattern p = Pattern.compile("^[a-z0-9_-]{3,15}$");
                Matcher m = p.matcher(login);
                return m.matches();
        }

        private static boolean checkEmail(String email){
                Pattern p = Pattern.compile("^([a-zA-Z0-9_\\.-]+)@([\\da-zA-Z\\.-]+)\\.([a-z\\.]{2,6})$");
                Matcher m = p.matcher(email);
                return m.matches();
        }

        private static boolean checkPassword(String password){
                Pattern p = Pattern.compile("^[а-яА-ЯёЁa-zA-Z0-9]+$");
                Matcher m = p.matcher(password);
                return m.matches();
        }

        public static boolean checkSignup(SignupRequest user) {

                if (!checkLogin(user.getLogin())) {
                        return false;
                }

                if (!checkEmail(user.getEmail())) {
                        return false;
                }

                if (!checkPassword(user.getPassword())) {
                        return false;
                }

                return true;
        }

        public static boolean checkSignin(SigninRequest user) {
                if (!checkLogin(user.getLogin())) {
                        return false;
                }

                if (!checkPassword(user.getPassword())) {
                        return false;
                }


                return true;

        }

        public static boolean checkSettings(SettingsRequest user)
        {
                if(!checkPassword(user.getPassword())){
                        return false;
                }
                if(user.getLogin() != null && user.getLogin().trim().length() != 0){
                        if(!checkLogin(user.getLogin())){
                                return false;
                        }
                }
                if(user.getEmail() != null && user.getEmail().trim().length() != 0){
                        if(!checkEmail(user.getEmail())){
                                return false;
                        }
                }
                if(user.getNewPassword() != null && user.getNewPassword().trim().length() != 0){
                        if(!checkPassword(user.getNewPassword())){
                                return false;
                        }
                }

                return true;

        }
;
}
