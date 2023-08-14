package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransactionDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;


import java.math.BigDecimal;
import java.util.List;

public class JdcbAccountDaoTest extends BaseDaoTests{

    private static final User BANK =
            new User(1001, "Bank", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "true");
    private static final User USER_1 =
            new User(1002, "bob", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "true");
    private static final User USER_2 =
            new User(1003, "user", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy", "true");

    private static final Account BANK_ACCOUNT =
            new Account(2001, 1001, BigDecimal.valueOf(1000000).setScale(2));
    private static final Account ACCOUNT_1 =
            new Account(2002, 1002, BigDecimal.valueOf(1001.00).setScale(2));
    private static final Account ACCOUNT_2 =
            new Account(2003, 1003, BigDecimal.valueOf(1000.00).setScale(2));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        TransactionDao transactionDao = new JdbcTransactionDao(jdbcTemplate);
        sut = new JdbcAccountDao(jdbcTemplate, transactionDao);
    }


    @Test
    public void getAllUsersAccounts_returns_all_accounts() {
        List<Account> actual = sut.getAllUsersAccounts();
        Assert.assertEquals(3, actual.size());
    }

    @Test
    public void getAccountIdByUsername_returns_account_id_for_bob() {
        int actual = sut.getAccountIdByUsername("bob");

        Assert.assertEquals(2002, actual);
    }

    @Test
    public void getAccountIdByUsername_returns_account_id_for_user() {
        int actual = sut.getAccountIdByUsername("user");

        Assert.assertEquals(2003, actual);
    }

    @Test
    public void getAccountIdByUsername_bad_value_returns_zero() {
        int actual = sut.getAccountIdByUsername("mine");

        Assert.assertEquals(0, actual);
    }


    @Test
    public void getAccountByUsername() {
        Account actual = sut.getAccountByUsername("bob");

        assertAccountsMatch(ACCOUNT_1, actual);

    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
        Assert.assertEquals(expected.getUserName(), actual.getUserName());
    }
}
