package com.techelevator.dao;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransactionDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcTransactionDao jdbcTransactionDao = new JdbcTransactionDao(jdbcTemplate);
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao(jdbcTemplate, jdbcTransactionDao);
        sut = new JdbcUserDao(jdbcTemplate, jdbcAccountDao);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

}
