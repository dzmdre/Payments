package Dao;

import Model.Payment;
import Model.PaymentUser;
import Model.UserRole;

import java.util.List;

/**
 * Created by computer on 13.11.2017.
 */
public interface UserDao extends CRUD<PaymentUser> {
    PaymentUser getUserByUsernameAndPassword(String username, String password);
    List<PaymentUser> getUserByRole(UserRole role);
}
