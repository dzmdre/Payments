import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.PaymentUser;
import model.UserRole;

import java.util.List;

/**
 * Created by computer on 13.11.2017.
 */
public class App {

    public static void main(String[] strings) {

        UserDao userDao = new UserDaoImpl();
        PaymentUser paymentUser = new PaymentUser();
        paymentUser.setUserRole(UserRole.USER);
        paymentUser.setUsername("user");
        paymentUser.setPassword("password");
        paymentUser.setUserId(1l);
        userDao.save(paymentUser);
        List<PaymentUser> users = userDao.getUserByRole(UserRole.USER);

    }
}
