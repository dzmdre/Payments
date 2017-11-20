package dao.impl;

import connection.ConnectionPool;
import dao.UserDao;
import model.PaymentUser;
import model.UserRole;

import java.sql.*;
import java.util.List;

/**
 * Created by computer on 19.11.2017.
 */
public class UserDaoImpl implements UserDao {

    @Override
    public List<PaymentUser> getUserByRole(UserRole role) {
        return null;
    }

    @Override
    public PaymentUser getUserByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Override
    public List<PaymentUser> getAll() {
        return null;
    }

    @Override
    public PaymentUser getByid(int id) {
        return null;
    }

    @Override
    public void save(PaymentUser entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            Statement statement = null;
            statement = connection.createStatement();



        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
    }

    @Override
    public PaymentUser extract(ResultSet result) {
        return null;
    }

    @Override
    public void delete(PaymentUser entity) {


    }
}
