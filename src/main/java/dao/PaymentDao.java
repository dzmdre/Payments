package dao;

import model.Payment;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by computer on 16.11.2017.
 */
public interface PaymentDao extends CRUD<Payment> {
    List<Payment> getPaymentsByAccount(Long accountId);
    List<Payment> getPaymentsByDateAndAccountId(LocalDate date, Long accountId);
}
