package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import io.jsonwebtoken.lang.Strings;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;
    private TransactionDao transactionDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate, TransactionDao transactionDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionDao = transactionDao;
    }


    @Override
    public List<Account> getAllUsersAccounts(){
        List<Account> usersAccounts = new ArrayList<>();
        String sql = "SELECT tenmo_user.username, account.user_id, account.account_id " +
                " FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Account account = mapRowToUsersAccounts(results);
            usersAccounts.add(account);
        }
        return usersAccounts;
    }


    @Override
    public Account getAccountByUsername(String username) {
        Account account = null;
        String sql = "SELECT * FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
            if (result.next()) {
                account = mapRowToAccount(result);
                BigDecimal accountBalance = transactionDao.getBalanceByAccountId(account.getAccountId());
                account.setBalance(accountBalance);
                return account;

            }
            throw new IllegalArgumentException("Account does not exist");
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (BadSqlGrammarException e) {
            System.out.println("SQL syntax error");
        }

        return account;
    }

    @Override
    public int getAccountIdByUsername(String username) {
        int accountId = 0;
        String sql = "SELECT * FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE username = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
            if (result.next()) {
                accountId = mapRowToAccount(result).getAccountId();
            }
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (BadSqlGrammarException e) {
            System.out.println("SQL syntax error");
        }
        return accountId;
    }


    @Override
    public void addBalance(int accountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";

        try{
            jdbcTemplate.update(sql, amount, accountId);
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (BadSqlGrammarException e) {
            System.out.println("SQL syntax error");
        }
    }

    @Override
    public void subtractBalance(int accountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";

        try{
            jdbcTemplate.update(sql, amount, accountId);
        } catch (CannotGetJdbcConnectionException e) {
            System.out.println("Unable to connect to server or database");
        } catch (BadSqlGrammarException e) {
            System.out.println("SQL syntax error");
        }
    }


    private Account mapRowToAccount(SqlRowSet result){
        Account account = new Account();
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }

    private Account mapRowToUsersAccounts(SqlRowSet result){
        Account account = new Account();
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        if(!StringUtils.isEmpty(result.getString("username")) ){
            account.setUserName(result.getString("username"));
        }
        return account;
    }


}
