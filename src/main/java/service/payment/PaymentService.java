package service.payment;

import connection.ConnectionPool;
import dao.AccountDao;
import dao.PaymentDao;
import model.Account;
import model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by computer on 04.12.2017.
 */
public class PaymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentDao paymentDao;
    private final AccountDao accountDao;

    public PaymentService(final PaymentDao paymentDao,
                          final AccountDao accountDao) {
        this.paymentDao = paymentDao;
        this.accountDao = accountDao;
    }


    public List<Payment> getByAccount(Long accountId) {
        return paymentDao.getPaymentsByAccount(accountId);
    }

    public List<Payment> getByAccountAndDate(Long accountId, LocalDate date) {
        return paymentDao.getPaymentsByDateAndAccountId(date, accountId);
    }

    public Payment createPayment(Payment payment) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            if (payment.getAccountId() == null) {
                throw new SQLException("Account Id can be empty");
            }
            connection.setAutoCommit(false);
            Account account = accountDao.getById(payment.getAccountId());

            if (account == null) {
                throw new SQLException("Retrieved account is null");
            }
            account.setSum(account.getSum() + payment.getSum());


            if (payment.getDate() == null) {
                payment.setDate(LocalDate.now());
            }
            accountDao.save(account, connection);
            paymentDao.save(payment, connection);
            connection.commit();
            return payment;
        } catch (SQLException e) {
            LOGGER.error("Error creating payment", e);
        }

        return null;
    }
}
