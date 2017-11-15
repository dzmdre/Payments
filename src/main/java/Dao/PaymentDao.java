package Dao;

import Model.Payment;

import java.util.Date;
import java.util.List;

/**
 * Created by computer on 16.11.2017.
 */
public interface PaymentDao extends CRUD<PaymentDao> {
    List<Payment> getPaymentsByAccount(Long accountId);
    List<Payment> getPaymentsByDateAndAccountId(Date date, Long accountId);
}
