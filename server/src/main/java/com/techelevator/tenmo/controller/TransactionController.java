package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class TransactionController {

    private UserDao userDao;

    private AccountDao accountDao;

    private TransactionDao transactionDao;

    public TransactionController(UserDao userDao, AccountDao accountDao, TransactionDao transactionDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfer/status/approved", method = RequestMethod.PUT)
    public Transaction updateTransferStatus(@RequestBody Transaction updatedTransaction, Principal principal){
        Transaction transaction = transactionDao.getTransactionDetailsById(updatedTransaction.getTransactionId());
        return transactionDao.updateTransactionStatusToApproved(transaction.getTransactionId(), "Approved");
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public Transaction addNewSendTransfer(@RequestBody Transaction transaction, Principal principal){
        int senderAccountId = accountDao.getAccountIdByUsername(principal.getName());
        transaction.setSenderAccountId(senderAccountId);
        transaction.setTransactionType("Send");
        return transactionDao.createTransaction(transaction, "Send");
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public Transaction addNewRequestTransfer(@RequestBody Transaction transaction, Principal principal){
        int accountId = accountDao.getAccountIdByUsername(principal.getName());
        transaction.setReceiverAccountId(accountId);
        transaction.setTransactionType("Request");
        return transactionDao.createTransaction(transaction, "Request");
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transaction showTransferById(@PathVariable int id){
        return transactionDao.getTransactionDetailsById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transaction> showAllMyTransfers(Principal principal){
        return transactionDao.getAllTransactionFromUser(principal.getName());
    }


}
