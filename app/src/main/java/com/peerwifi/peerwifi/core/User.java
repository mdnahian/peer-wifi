package com.peerwifi.peerwifi.core;

import java.io.Serializable;

/**
 * Created by mdislam on 4/2/16.
 */
public class User implements Serializable {

    private String username;
    private String password;
    private String payPalEmail;
    private BankingInfo bankingInfo;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayPalEmail() {
        return payPalEmail;
    }

    public void setPayPalEmail(String payPalEmail) {
        this.payPalEmail = payPalEmail;
    }

    public BankingInfo bankingInfo() {
        return bankingInfo;
    }

}
