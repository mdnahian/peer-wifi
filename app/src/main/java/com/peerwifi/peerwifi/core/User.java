package com.peerwifi.peerwifi.core;

/**
 * Created by mdislam on 4/2/16.
 */
public class User {

    private String username;
    private String password;
    private String payPalEmail;
    private BankingInfo bankingInfo;

    public User(String username, String password, String payPalEmail, BankingInfo bankingInfo) {
        this.username = username;
        this.password = password;
        this.payPalEmail = payPalEmail;
        this.bankingInfo = bankingInfo;
    }

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
