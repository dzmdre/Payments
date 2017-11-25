package dao.impl;

import dao.UserDao;
import model.PaymentUser;
import model.UserRole;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

/**
 * Created by computer on 25.11.2017.
 */

public class UserDaoImplTest {

    private PaymentUser paymentUser;
    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        paymentUser = new PaymentUser();
        userDao = new UserDaoImpl();
        paymentUser.setUsername("USER_TEST_NAME");
        paymentUser.setPassword("password");
        paymentUser.setUserRole(UserRole.ADMIN);
        paymentUser = userDao.save(paymentUser);
    }

    @After
    public void tearDown() throws Exception {
        if (userDao.getById(paymentUser.getUserId()) != null) {
            userDao.delete(paymentUser);
        }
    }

    @Test
    public void getUserByRole() throws Exception {
        List<PaymentUser> userList = userDao.getUserByRole(UserRole.ADMIN);
        Optional<PaymentUser> admin = userList.stream().filter(u -> u.getUserId() == paymentUser.getUserId()).findFirst();
        Assert.assertTrue("should find user by role", admin.isPresent());
    }

    @Test
    public void getUserByUsername() throws Exception {
        PaymentUser userByUsername = userDao.getUserByUsername(paymentUser.getUsername());
        Assert.assertNotNull("should find user by username", userByUsername);
    }

    @Test
    public void getAll() throws Exception {
        List<PaymentUser> userList = userDao.getAll();
        Assert.assertTrue("should all users", userList.size() > 0);
    }

    @Test
    public void getById() throws Exception {
        PaymentUser userById = userDao.getById(paymentUser.getUserId());
        Assert.assertNotNull("should find user by id", userById);
    }

    @Test
    public void save() throws Exception {
        String newPassword= "newPassword";
        paymentUser.setPassword(newPassword);
        userDao.save(paymentUser);
        PaymentUser updatedUser = userDao.getById(paymentUser.getUserId());
        Assert.assertEquals("password should be updated", updatedUser.getPassword(), newPassword);

        PaymentUser newUser = userDao.save(createNewPaymentUser());
        newUser = userDao.getById(newUser.getUserId());
        Assert.assertNotNull("new user should be saved", newUser);

        userDao.delete(newUser);

        PaymentUser userWithDuplicatesUsername = new PaymentUser();
        userWithDuplicatesUsername.setPassword("asdasd");
        userWithDuplicatesUsername.setUsername(paymentUser.getUsername());

        userWithDuplicatesUsername = userDao.save(userWithDuplicatesUsername);

        Assert.assertNull("new user with duplicated username should not be saved", userWithDuplicatesUsername);
    }

    @Test
    public void delete() throws Exception {
        userDao.delete(paymentUser);
        Assert.assertNull("user should be deleted", userDao.getById(paymentUser.getUserId()));
    }


    private PaymentUser createNewPaymentUser() {
        PaymentUser paymentUser = new PaymentUser();
        paymentUser.setUsername("USER_TEST_NAME_NEW1");
        paymentUser.setPassword("password");
        paymentUser.setUserRole(UserRole.ADMIN);
        return paymentUser;
    }

}