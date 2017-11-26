package dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import connection.ConnectionPool;
import dao.AccountDao;
import model.Account;
import utll.PreparedStatementHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 25.11.2017.
 */
public class AccountDaoImpl extends AbstractDao<Account> implements AccountDao {


    @Override
    public Account getById(Long id) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from account WHERE account_id=?");
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
    public List<Account> getAccountByUser(Long userId) {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from account WHERE user_id=?");
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                accounts.add(extract(resultSet));
            }
            return accounts;

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accounts.add(extract(resultSet));
            }
            return accounts;

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public Account getAccountByCard(Long cardId) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from account WHERE card_id=?");
            ps.setLong(1, cardId);
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
    public Account save(Account entity) {
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
    public Account save(Account entity, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        boolean isSaving = true;

        if (entity.getAccountId() == null || getById(entity.getAccountId()) == null) {
            ps = createSaveStatement(connection, entity);
        } else {
            ps = createUpdateStatement(connection, entity);
            isSaving = false;
        }
        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating account failed, no rows affected.");
        }
        if (isSaving) {
            extractNewKeyBySaving(ps, entity);
        }
        ps.close();
        return entity;
    }

    @Override
    public Account delete(Account entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE from account WHERE account_id=?");
            ps.setLong(1, entity.getAccountId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account extract(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setUserId(rs.getLong("user_id") );
        account.setAccountNumber(rs.getString("account_number") );
        account.setAccountId(rs.getLong("account_id") );
        account.setCardId(rs.getLong("card_id"));
        account.setLocked(rs.getBoolean("locked"));
        account.setSum(rs.getLong("sum"));
        return account;
    }

    protected PreparedStatement createSaveStatement(Connection connection, Account entity)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO account (locked, account_number, sum, user_id, card_id) VALUES (?, ?, ?, ? ,?)", Statement.RETURN_GENERATED_KEYS);


        PreparedStatementHelper.setBooleanOrNull(ps, 1, entity.getLocked());
        ps.setString(2, entity.getAccountNumber());

        PreparedStatementHelper.setLongOrNull(ps, 3, entity.getSum());
        PreparedStatementHelper.setLongOrNull(ps, 4, entity.getUserId());
        PreparedStatementHelper.setLongOrNull(ps, 5, entity.getCardId());

        return ps;
    }

    protected PreparedStatement createUpdateStatement(Connection connection, Account entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE account SET locked=?, account_number=?, sum=?, user_id=?, card_id=?  WHERE account_id=?");

        PreparedStatementHelper.setBooleanOrNull(ps, 1, entity.getLocked());
        ps.setString(2, entity.getAccountNumber());
        PreparedStatementHelper.setLongOrNull(ps, 3, entity.getSum());
        PreparedStatementHelper.setLongOrNull(ps, 4, entity.getUserId());
        PreparedStatementHelper.setLongOrNull(ps, 5, entity.getCardId());
        PreparedStatementHelper.setLongOrNull(ps, 6, entity.getAccountId());
        return ps;
    }

    protected void extractNewKeyBySaving(PreparedStatement ps, Account entity) throws SQLException {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                entity.setAccountId(generatedKeys.getLong(1));
            }
            else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }
        }
    }
}
