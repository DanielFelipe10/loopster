package com.example.looptser.auth;

import java.util.Objects;

public class Auth {
    public boolean validateSignupData(String name, String email, String password, String confirmPassword) {
        if(!Objects.equals(name, "") && !Objects.equals(email, "") && !Objects.equals(password, "") && !Objects.equals(confirmPassword, "")) {
            return password.equals(confirmPassword);
        } else {
            return false;
        }
    }

    public boolean validateSigninData(String name, String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.matches(emailPattern)) {
            return !Objects.equals(name, "") && !Objects.equals(email, "");
        } else {
            return false;
        }
    }
}
