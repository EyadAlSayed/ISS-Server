package com.iss.info.security.system.model;

public class SignupModel {

    String name;
    String phoneNumber;
    String password;
    String sessionKey;
    String userPublicKey;

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getUserPublicKey() {
        return userPublicKey;
    }
}
