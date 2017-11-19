package dao;

import model.PaymentUser;
import model.UserRole;

import java.util.List;

/**
 * Created by computer on 13.11.2017.
 */
public interface UserDao extends CRUD<PaymentUser> {
    PaymentUser getUserByUsernameAndPassword(String username, String password);
    List<PaymentUser> getUserByRole(UserRole role);
}
