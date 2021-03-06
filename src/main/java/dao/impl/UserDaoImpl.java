package dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import connection.ConnectionPool;
import dao.UserDao;
import model.PaymentUser;
import model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 19.11.2017.
 */
public class UserDaoImpl extends AbstractDao<PaymentUser> implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String DAO_ERROR = "Dao error";


    @Override
    public List<PaymentUser> getUserByRole(UserRole role) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from payment_user WHERE user_role=?");
            ps.setString(1, role.getRoleId());
            ResultSet rs = ps.executeQuery();

            List<PaymentUser> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(extract(rs));
            }
            return userList;

        } catch (SQLException e) {
            LOGGER.error(DAO_ERROR,e);
        }
        return null;
    }

    @Override
    public PaymentUser getUserByUsername(String username) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from payment_user WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return extract(rs);
            }

        } catch (SQLException e) {
            LOGGER.error(DAO_ERROR,e);
        }
        return null;
    }

    @Override
    public List<PaymentUser> getAll() {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from payment_user;");

            List<PaymentUser> userList = new ArrayList<>();
            while (rs.next()) {
                userList.add(extract(rs));
            }
            return userList;

        } catch (SQLException e) {
            LOGGER.error(DAO_ERROR,e);
        }
        return null;
    }


    @Override
    public PaymentUser getById(Long id) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from payment_user WHERE user_id=?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return extract(rs);
            }

        } catch (SQLException e) {
            LOGGER.error(DAO_ERROR,e);
        }
        return null;
    }

    @Override
    public PaymentUser save(PaymentUser entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {

            return this.save(entity, connection);

        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.error(DAO_ERROR,e);
        } catch (SQLException e) {
            LOGGER.error(DAO_ERROR,e);
        }
        return null;
    }

    protected PreparedStatement createSaveStatement(Connection connection, PaymentUser entity)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO payment_user (username, password, user_role) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, entity.getUsername());
        ps.setString(2, entity.getPassword());
        ps.setString(3, entity.getUserRole() != null ? entity.getUserRole().name() : null);
        return ps;
    }

    protected void extractNewKeyBySaving(PreparedStatement ps, PaymentUser paymentUser) throws SQLException {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                paymentUser.setUserId(generatedKeys.getLong(1));
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    protected PreparedStatement createUpdateStatement(Connection connection, PaymentUser entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE payment_user SET username=?, password=?, user_role=? WHERE user_id=?");
        ps.setString(1, entity.getUsername());
        ps.setString(2, entity.getPassword());
        ps.setString(3, entity.getUserRole() != null ? entity.getUserRole().name() : null);
        ps.setLong(4, entity.getUserId());
        return ps;
    }


    @Override
    public PaymentUser extract(ResultSet rs) throws SQLException {
        PaymentUser user = new PaymentUser();
        user.setUserId(rs.getLong("user_id") );
        user.setUsername(rs.getString("username") );
        user.setPassword(rs.getString("password") );
        user.setUserRole(UserRole.valueOf(rs.getString("user_role")));
        return user;
    }

    @Override
    public PaymentUser delete(PaymentUser entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE from payment_user WHERE user_id=?");
            ps.setLong(1, entity.getUserId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException e) {
            LOGGER.error(DAO_ERROR,e);
        }
        return null;
    }

    @Override
    public PaymentUser save(PaymentUser entity, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        boolean isSaving = true;

        if (entity.getUserId() == null || getById(entity.getUserId()) == null) {
            ps = createSaveStatement(connection, entity);
        } else {
            ps = createUpdateStatement(connection, entity);
            isSaving = false;
        }

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        if (isSaving) {
            extractNewKeyBySaving(ps, entity);
        }

        ps.close();
        return entity;
    }
}
