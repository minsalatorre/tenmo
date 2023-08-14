package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.naming.NameAlreadyBoundException;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private JdbcTemplate jdbcTemplate;
//    private AccountDao accountDao;

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transaction createTransaction(Transaction transaction, String transactionType) {

        String sql = "INSERT INTO transactions (transaction_type, sender_account_id, receiver_account_id, status, transfer_amount, message_text) " +
                " VALUES(?, ?, ?, ?, ?, ?) RETURNING transaction_id ";

        try {
            if (transactionType == "Request") {
                transaction.setStatus("Pending");
            } else if (transactionType == "Send") {
                transaction.setStatus("Approved");
            } else {
                throw new IllegalArgumentException("Not enough funds to make transfer");
            }

            Integer newTransactionId = jdbcTemplate.queryForObject(sql, Integer.class, transaction.getTransactionType(), transaction.getSenderAccountId(),
                    transaction.getReceiverAccountId(), transaction.getStatus(), transaction.getTransferAmount(), transaction.getMessageText());
            transaction.setTransactionId(newTransactionId);
            transaction.getTransactionId();
//            accountDao.addBalance(transaction.getReceiverAccountId(), transaction.getTransferAmount());
//            accountDao.subtractBalance(transaction.getSenderAccountId(), transaction.getTransferAmount());
            return transaction;
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Bad request");
        }
    }

    @Override
    public Transaction updateTransactionStatusToApproved(int transactionId, String transactionStatus) {

        String sql = "UPDATE transactions SET status = ? " +
                " WHERE transaction_id = ? ";

        try {
            Transaction transaction = getTransactionDetailsById(transactionId);

            if (transactionIsValid(transaction)) {
                jdbcTemplate.update(sql, "Approved", transactionId);
            } else {
                jdbcTemplate.update(sql, "Rejected", transactionId);
                throw new IllegalArgumentException("User does not have enough balance");
            }

            Transaction updatedTransaction = getTransactionDetailsById(transactionId);

            return updatedTransaction;
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Bad request");
        }
    }

    @Override
    public List<Transaction> getAllTransactionFromUser(String username) {
        List<Transaction> allTransaction = new ArrayList<>();
        String sql = "SELECT * " +
                " FROM transactions JOIN account ON transactions.sender_account_id = account.account_id OR transactions.receiver_account_id = account.account_id " +
                " JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                " WHERE tenmo_user.username = ?;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);

            while (result.next()) {
                allTransaction.add(mapRowToTransaction(result));
            }

        } catch (CannotGetJdbcConnectionException e) {
                System.out.println("Unable to connect to server or database");
            } catch (BadSqlGrammarException e) {
                System.out.println("SQL syntax error");
            }

        return allTransaction;

    }


    @Override
    public Transaction getTransactionDetailsById(int transactionId) {
        Transaction transaction = null;
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";

        try{
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transactionId);
            if(result.next()){
                transaction = mapRowToTransaction(result);
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (BadSqlGrammarException e) {
            System.out.println("SQL syntax error");
        }
        return transaction;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        String sql = "SELECT * FROM transactions WHERE status = ? AND (sender_account_id = ? OR receiver_account_id = ?);";
        List<Transaction> allTransaction = new ArrayList<>();
        BigDecimal deposits = new BigDecimal(0);
        BigDecimal withdrawal = new BigDecimal(0);

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "Approved", accountId, accountId);
            while (results.next()) {
                allTransaction.add(mapRowToTransaction(results));
            }
            for(Transaction transaction : allTransaction) {
                if (transaction.getReceiverAccountId() == accountId) {
                    deposits = deposits.add(transaction.getTransferAmount());
                } else if (transaction.getSenderAccountId() == accountId) {
                    withdrawal = withdrawal.add(transaction.getTransferAmount());
                }
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (BadSqlGrammarException e) {
            System.out.println("SQL syntax error");
        }
        return deposits.subtract(withdrawal);
    }

    private boolean transactionIsValid(Transaction transaction) {
        BigDecimal senderBalance = getBalanceByAccountId(transaction.getSenderAccountId());

        if (senderBalance.subtract(transaction.getTransferAmount()).compareTo(BigDecimal.ZERO) >= 0) {
            return true;

        }
        return false;
    }

    private Transaction mapRowToTransaction(SqlRowSet result){
        Transaction transaction = new Transaction();
        transaction.setTransactionId(result.getInt("transaction_id"));
        transaction.setMessageText(result.getString("message_text"));
        transaction.setReceiverAccountId(result.getInt("receiver_account_id"));
        transaction.setSenderAccountId(result.getInt("sender_account_id"));
        transaction.setStatus(result.getString("status"));
        transaction.setTransferAmount(result.getBigDecimal("transfer_amount"));
        transaction.setTransactionType(result.getString("transaction_type"));
        return transaction;
    }



}
