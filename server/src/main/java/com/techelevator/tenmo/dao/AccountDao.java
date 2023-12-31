package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAllUsersAccounts();
    Account getAccountByUsername(String username);
    int getAccountIdByUsername(String username);
    void addBalance(int accountId, BigDecimal amount);
    void subtractBalance(int accountId, BigDecimal amount);
}
