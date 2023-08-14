package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private int userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal balance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Account(int userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Account() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}
