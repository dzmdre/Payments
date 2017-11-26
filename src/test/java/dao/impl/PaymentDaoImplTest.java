package dao.impl;

import dao.AccountDao;
import dao.PaymentDao;
import model.Account;
import model.Payment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by computer on 26.11.2017.
 */
public class PaymentDaoImplTest {


    private final AccountDao accountDao = new AccountDaoImpl();
    private final PaymentDao paymentDao = new PaymentDaoImpl();

    private Account account;
    private LocalDate localDate;
    private Payment payment;

    @Before
    public void setUp() throws Exception {
        account = new Account();
        account.setAccountNumber("131sf");
        accountDao.save(account);
        localDate = LocalDate.now();
        payment = createPayment(42l);
        paymentDao.save(payment);
    }

    @After
    public void tearDown() throws Exception {
        paymentDao.delete(payment);
        accountDao.delete(account);
    }

    @Test
    public void getById() throws Exception {
        Payment payment = paymentDao.getById(this.payment.getPaymentId());
        Assert.assertNotNull(payment);
        Assert.assertEquals(this.payment.getPaymentId(), payment.getPaymentId());
    }

    @Test
    public void getAll() throws Exception {
        List<Payment> payments = paymentDao.getAll();
        Assert.assertTrue(payments.stream().filter(p -> p.getPaymentId() == payment.getPaymentId()).findFirst().isPresent());
    }

    @Test
    public void save() throws Exception {
        Long newSum = 11l;
        payment.setSum(newSum);
        paymentDao.save(payment);
        Assert.assertEquals(newSum, paymentDao.getById(payment.getPaymentId()).getSum());

        Payment newPayment = createPayment(1212l);
        paymentDao.save(newPayment);

        Assert.assertNotNull(paymentDao.getById(newPayment.getPaymentId()));
        paymentDao.delete(newPayment);
    }

    @Test
    public void delete() throws Exception {
        Payment payment = createPayment(11l);
        paymentDao.save(payment);
        paymentDao.delete(payment);
        Assert.assertNull(paymentDao.getById(payment.getPaymentId()));
    }

    @Test
    public void getPaymentsByAccount() throws Exception {
        List<Payment> payments = paymentDao.getPaymentsByAccount(account.getAccountId());
        Assert.assertTrue(payments.stream().filter(p ->p.getPaymentId() == payment.getPaymentId()).findFirst().isPresent());
    }

    @Test
    public void getPaymentsByDateAndAccountId() throws Exception {
        List<Payment> payments = paymentDao.getPaymentsByDateAndAccountId(localDate ,account.getAccountId());
        Assert.assertTrue(payments.stream().filter(p ->p.getPaymentId() == payment.getPaymentId()).findFirst().isPresent());
    }


    private Payment createPayment(Long sum) {
        Payment payment = new Payment();
        payment.setSum(sum);
        payment.setDate(localDate);
        payment.setAccountId(account.getAccountId());
        return payment;
    }
}