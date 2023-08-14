package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransactionDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionDaoTest extends BaseDaoTests{

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

    private static final Transaction TRANSACTION_WELCOME_1 =
            new Transaction(3001, "Send", 2001, 2002, "Approved", BigDecimal.valueOf(1000.00).setScale(2), "Welcome to Tenmo Bank!");
    private static final Transaction TRANSACTION_WELCOME_2 =
            new Transaction(3002, "Send", 2001, 2003, "Approved", BigDecimal.valueOf(1000.00).setScale(2), "Welcome to Tenmo Bank!");
    private static final Transaction TRANSACTION_1 =
            new Transaction(3003, "Send", 2002, 2003, "Approved", BigDecimal.valueOf(150.00).setScale(2), "bob sending to user");
    private static final Transaction TRANSACTION_2 =
            new Transaction(3004, "Send", 2003, 2002, "Approved", BigDecimal.valueOf(151.00).setScale(2), "user sending to bob");

    private JdbcTransactionDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransactionDao(jdbcTemplate);
    }

    @Test
    public void createTransaction_creates_new_transaction() {
        Transaction transaction = new Transaction("Send", 2001, 2002, "Approved", BigDecimal.valueOf(200.), "Test message");
        Transaction actual = sut.createTransaction(transaction, transaction.getTransactionType());

        Assert.assertEquals(transaction, actual);
    }

    @Test
    public void getAllTransactionsFromUser_returns_transactions_for_bob() {
        List<Transaction> actual = sut.getAllTransactionFromUser("bob");

        Assert.assertEquals(3, actual.size());

        assertTransactionsMatch(TRANSACTION_WELCOME_1, actual.get(0));
        assertTransactionsMatch(TRANSACTION_1, actual.get(1));
        assertTransactionsMatch(TRANSACTION_2, actual.get(2));
    }

    @Test
    public void getAllTransactionsFromUser_returns_transactions_for_user() {
        List<Transaction> actual = sut.getAllTransactionFromUser("user");

        Assert.assertEquals(3, actual.size());

        assertTransactionsMatch(TRANSACTION_WELCOME_2, actual.get(0));
        assertTransactionsMatch(TRANSACTION_1, actual.get(1));
        assertTransactionsMatch(TRANSACTION_2, actual.get(2));
    }

    @Test
    public void getTransactionDetailsById_transaction_one() {
        Transaction actual = sut.getTransactionDetailsById(3003);

        assertTransactionsMatch(TRANSACTION_1, actual);
    }

    @Test
    public void getTransactionDetailsById_transaction_two() {
        Transaction actual = sut.getTransactionDetailsById(3004);

        assertTransactionsMatch(TRANSACTION_2, actual);
    }

    @Test
    public void getTransactionDetailsById_null_test() {
        Transaction actual = sut.getTransactionDetailsById(3015);

        Assert.assertNull(null, actual);
    }

    @Test
    public void getBalanceByAccountId_bob() {
        BigDecimal actual = sut.getBalanceByAccountId(2002);

        Assert.assertEquals(BigDecimal.valueOf(1001.00).setScale(2), actual);
    }

    @Test
    public void getBalanceByAccountId_user() {
        BigDecimal actual = sut.getBalanceByAccountId(2003);

        Assert.assertEquals(BigDecimal.valueOf(999.00).setScale(2), actual);
    }

    @Test
    public void getBalanceByAccountId_no_valid_account() {
        BigDecimal actual = sut.getBalanceByAccountId(9999);

        Assert.assertEquals(BigDecimal.valueOf(0), actual);
    }
    private void assertTransactionsMatch(Transaction expected, Transaction actual) {
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
        Assert.assertEquals(expected.getSenderAccountId(), actual.getSenderAccountId());
        Assert.assertEquals(expected.getReceiverAccountId(), actual.getReceiverAccountId());
        Assert.assertEquals(expected.getTransactionId(), actual.getTransactionId());
        Assert.assertEquals(expected.getTransactionType(), actual.getTransactionType());
        Assert.assertEquals(expected.getMessageText(), actual.getMessageText());
        Assert.assertEquals(expected.getStatus(), actual.getStatus());


    }
}
