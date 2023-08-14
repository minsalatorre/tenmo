package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.security.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/get-users", method = RequestMethod.GET)
    public List<Account> getUsersAccounts(){
        String currentUser = SecurityUtils.getCurrentUsername().orElse("");
        List<Account> usersAccounts = accountDao.getAllUsersAccounts().stream().filter(userAccount-> !userAccount.getUserName().equals(currentUser)).collect(Collectors.toList());
        return usersAccounts;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal){
        Account account = accountDao.getAccountByUsername(principal.getName());
        return account.getBalance();
    }

}
