package com.iss.info.security.system.model.req;

import java.io.Serializable;

public class LoginModel implements Serializable {
    int userId;
    String phoneNumber;
    String password;


    public LoginModel() {
    }

    public LoginModel(int userId, String phoneNumber, String password) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public LoginModel(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
