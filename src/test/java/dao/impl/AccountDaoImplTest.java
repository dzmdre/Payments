package dao.impl;

import dao.AccountDao;
import dao.CardDao;
import dao.UserDao;
import model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by computer on 25.11.2017.
 */
public class AccountDaoImplTest {

    private final AccountDao accountDao = new AccountDaoImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final CardDao cardDao = new CardDaoImpl();

    private Account account;
    private PaymentUser paymentUser;
    private Card card;


    @Before
    public void setUp() throws Exception {
        paymentUser = new PaymentUser();
        paymentUser.setUsername("USER_TEST_NAME");
        paymentUser.setPassword("password");
        paymentUser.setUserRole(UserRole.ADMIN);
        paymentUser = userDao.save(paymentUser);

        card = new Card();
        card.setCardNumber("123123");
        card.setCardType(CardType._MASTER_CARD);
        cardDao.save(card);

        account = new Account();
        account.setAccountNumber("123213");
        account.setUserId(paymentUser.getUserId());
        account.setCardId(card.getCardId());
        accountDao.save(account);
    }

    @After
    public void tearDown() throws Exception {

        if (account != null && accountDao.getById(account.getAccountId()) != null) {
            account.setUserId(null);
            account.setCardId(null);
            accountDao.save(account);
            accountDao.delete(account);
        }

        if (card != null && cardDao.getById(card.getCardId()) != null) {
            card.setUserId(null);
            card.setAccountId(null);
            cardDao.save(card);
            cardDao.delete(card);
        }


        if (paymentUser != null && userDao.getById(paymentUser.getUserId()) != null) {
            userDao.delete(paymentUser);
        }

    }

    @Test
    public void getById() throws Exception {
        Account accountById = accountDao.getById(account.getAccountId());
        Assert.assertNotNull("should find account by id", accountById);
    }

    @Test
    public void getAccountByUser() throws Exception {
        List<Account> accountByUser = accountDao.getAccountByUser(paymentUser.getUserId());
        Optional<Account> accountOptional = accountByUser.stream().filter(a -> a.getAccountId() == account.getAccountId()).findFirst();
        Assert.assertTrue(accountOptional.isPresent());
    }

    @Test
    public void getAll() throws Exception {
        List<Account> accounts = accountDao.getAll();
        Optional<Account> acc = accounts.stream().filter(a -> a.getAccountId() == account.getAccountId()).findFirst();
        Assert.assertTrue(acc.isPresent());
    }

    @Test
    public void getAccountByCard() throws Exception {
        Account acc = accountDao.getAccountByCard(card.getCardId());
        Assert.assertNotNull(acc);
    }

    @Test
    public void save() throws Exception {
        Long newSum = 42l;
        account.setSum(newSum);
        accountDao.save(account);
        Assert.assertEquals(accountDao.getById(account.getAccountId()).getSum(), newSum);

        Account newAccount = new Account();
        newAccount.setAccountNumber("333");
        accountDao.save(newAccount);

        Assert.assertNotNull(accountDao.getById(newAccount.getAccountId()));

        accountDao.delete(newAccount);
    }

    @Test
    public void delete() throws Exception {

        Account newAccount = new Account();
        newAccount.setAccountNumber("444");
        accountDao.save(newAccount);

        accountDao.delete(newAccount);
        Assert.assertNull(accountDao.getById(newAccount.getAccountId()));

    }

}