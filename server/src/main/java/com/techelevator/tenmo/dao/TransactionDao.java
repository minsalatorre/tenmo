package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDao {
    Transaction createTransaction(Transaction transaction, String transactionType);
    List<Transaction> getAllTransactionFromUser(String username);
    Transaction getTransactionDetailsById(int transactionId);
    BigDecimal getBalanceByAccountId(int accountId);
    Transaction updateTransactionStatusToApproved(int transactionId, String transactionStatus);
}
