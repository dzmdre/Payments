package dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import connection.ConnectionPool;
import dao.PaymentDao;
import model.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 25.11.2017.
 */
public class PaymentDaoImpl extends AbstractDao<Payment> implements PaymentDao {

    @Override
    public Payment getById(Long id) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from payment WHERE payment_id=?;");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return extract(rs);
            }

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> getAll() {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from payment;");

            List<Payment> paymentList = new ArrayList<>();
            while (rs.next()) {
                paymentList.add(extract(rs));
            }
            return paymentList;

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Payment save(Payment entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {

            return this.save(entity, connection);

        } catch (MySQLIntegrityConstraintViolationException e) {
            // TODO
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Payment save(Payment entity, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        boolean isSaving = true;

        if (entity.getPaymentId() == null || getById(entity.getPaymentId()) == null) {
            ps = createSaveStatement(connection, entity);
        } else {
            ps = createUpdateStatement(connection, entity);
            isSaving = false;
        }

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating paymnet failed, no rows affected.");
        }

        if (isSaving) {
            extractNewKeyBySaving(ps, entity);
        }

        ps.close();
        return entity;
    }

    @Override
    public Payment delete(Payment entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE from payment WHERE payment_id=?");
            ps.setLong(1, entity.getPaymentId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting payment failed, no rows affected.");
            }

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Payment extract(ResultSet result) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(result.getLong("payment_id"));
        payment.setAccountId(result.getLong("account_id"));
        payment.setSum(result.getLong("sum"));
        payment.setDate(result.getDate("payment_date") != null ? result.getDate("payment_date").toLocalDate() : null);
        return payment;
    }


    @Override
    public List<Payment> getPaymentsByAccount(Long accountId) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from payment WHERE account_id=?;");
            statement.setLong(1, accountId);
            ResultSet rs = statement.executeQuery();
            List<Payment> paymentList = new ArrayList<>();
            while (rs.next()) {
                paymentList.add(extract(rs));
            }
            return paymentList;

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> getPaymentsByDateAndAccountId(LocalDate date, Long accountId) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * from payment WHERE account_id=? AND payment_date=?;");
            statement.setLong(1, accountId);
            statement.setDate(2, date != null ? Date.valueOf(date) : null);
            ResultSet rs = statement.executeQuery();
            List<Payment> paymentList = new ArrayList<>();
            while (rs.next()) {
                paymentList.add(extract(rs));
            }
            return paymentList;

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected PreparedStatement createSaveStatement(Connection connection, Payment entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO payment (payment_date, sum, account_id) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ps.setDate(1, entity.getDate() != null ? Date.valueOf(entity.getDate()) : null);
        ps.setLong(2, entity.getSum());
        ps.setLong(3, entity.getAccountId());
        return ps;
    }

    @Override
    protected PreparedStatement createUpdateStatement(Connection connection, Payment entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("UPDATE payment SET payment_date=?, sum=?, account_id=? WHERE payment_id=?");
        ps.setDate(1, entity.getDate() != null ? Date.valueOf(entity.getDate()) : null);
        ps.setLong(2, entity.getSum());
        ps.setLong(3, entity.getAccountId());
        return ps;

    }

    @Override
    protected void extractNewKeyBySaving(PreparedStatement ps, Payment entity) throws SQLException {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                entity.setPaymentId(generatedKeys.getLong(1));
            }
            else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }
        }

    }
}
