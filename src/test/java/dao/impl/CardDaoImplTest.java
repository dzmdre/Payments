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
 * Created by computer on 26.11.2017.
 */
public class CardDaoImplTest {

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

        account = new Account();
        account.setAccountNumber("123213");
        accountDao.save(account);

        card = new Card();
        card.setCardNumber("123123");
        card.setCardType(CardType._MASTER_CARD);
        card.setUserId(paymentUser.getUserId());
        card.setAccountId(account.getAccountId());
        card = cardDao.save(card);

    }

    @After
    public void tearDown() throws Exception {

        if (card != null && cardDao.getById(card.getCardId()) != null) {
            card.setUserId(null);
            card.setAccountId(null);
            cardDao.save(card);
            cardDao.delete(card);
        }

        if (account != null && accountDao.getById(account.getAccountId()) != null) {
            accountDao.delete(account);
        }

        if (paymentUser != null && userDao.getById(paymentUser.getUserId()) != null) {
            userDao.delete(paymentUser);
        }
    }

    @Test
    public void getCardByUser() throws Exception {
        List<Card> cardList = cardDao.getCardByUser(paymentUser.getUserId());
        Optional<Card> card = cardList.stream().filter(c -> c.getCardId() == this.card.getCardId()).findFirst();
        Assert.assertTrue(card.isPresent());
    }

    @Test
    public void getCardByAccount() throws Exception {
        Card card = cardDao.getCardByAccount(account.getAccountId());
        Assert.assertNotNull(card);
        Assert.assertEquals(card.getCardId(), this.card.getCardId());
    }

    @Test
    public void getById() throws Exception {
        Card card = cardDao.getById(this.card.getCardId());
        Assert.assertEquals(card.getCardId(), this.card.getCardId());
    }

    @Test
    public void getAll() throws Exception {
        List<Card> cardList = cardDao.getAll();
        Optional<Card> card = cardList.stream().filter(a -> a.getCardId() == this.card.getCardId()).findFirst();
        Assert.assertTrue(card.isPresent());
    }

    @Test
    public void save() throws Exception {

        String newType = CardType._VISA;
        card.setCardType(newType);

        cardDao.save(card);

        Assert.assertEquals(newType, cardDao.getById(card.getCardId()).getCardType());

        Card newCard = new Card();
        newCard.setCardNumber("1fdsg349t3g j3wexx  ");
        cardDao.save(newCard);

        Assert.assertNotNull(cardDao.getById(newCard.getCardId()));
        cardDao.delete(newCard);

    }

    @Test
    public void delete() throws Exception {

        Card newCard = new Card();
        newCard.setCardNumber("1fdsg349t3g j3wexx  ");
        cardDao.save(newCard);

        Assert.assertNotNull(cardDao.getById(newCard.getCardId()));

        cardDao.delete(newCard);

        Assert.assertNull(cardDao.getById(newCard.getCardId()));
    }

}