package dao.impl;

import connection.ConnectionPool;
import dao.CardDao;
import model.Card;
import utll.PreparedStatementHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by computer on 25.11.2017.
 */
public class CardDaoImpl extends AbstractDao<Card> implements CardDao {

    @Override
    public List<Card> getCardByUser(Long userId) {
        List<Card> cards = new ArrayList<>();
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from card WHERE user_id=?;");
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                cards.add(extract(resultSet));
            }
            return cards;

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Card getCardByAccount(Long accountId) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from card WHERE account_id=?;");
            ps.setLong(1, accountId);
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
    public Card getById(Long id) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * from card WHERE card_id=?;");
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
    public List<Card> getAll() {
        List<Card> cards = new ArrayList<>();
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM card;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cards.add(extract(resultSet));
            }
            return cards;

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public Card save(Card entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {

           return this.save(entity, connection);

        }catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Card save(Card entity, Connection connection) throws SQLException {
        PreparedStatement ps = null;
        boolean isSaving = true;

        if (entity.getCardId() == null || getById(entity.getCardId()) == null) {
            ps = createSaveStatement(connection, entity);
        } else {
            ps = createUpdateStatement(connection, entity);
            isSaving = false;
        }

        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating card failed, no rows affected.");
        }

        if (isSaving) {
            extractNewKeyBySaving(ps, entity);
        }

        ps.close();
        return entity;
    }

    @Override
    public Card delete(Card entity) {
        try(Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE from card WHERE card_id=?");
            ps.setLong(1, entity.getCardId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating card failed, no rows affected.");
            }

        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Card extract(ResultSet result) throws SQLException {
        Card card = new Card();
        card.setCardId(result.getLong("card_id"));
        card.setAccountId(result.getLong("account_id"));
        card.setUserId(result.getLong("user_id"));
        card.setCardDate(result.getDate("card_date") != null ? result.getDate("card_date").toLocalDate() : null);
        card.setCardType(result.getString("card_type"));
        card.setCardNumber(result.getString("card_number"));
        return card;
    }

    @Override
    protected PreparedStatement createSaveStatement(Connection connection, Card entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO card (card_number, card_type, card_date, account_id, user_id) VALUES (?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, entity.getCardNumber());
        ps.setString(2, entity.getCardType());
        PreparedStatementHelper.setDate(ps, 3, entity.getCardDate());
        PreparedStatementHelper.setLongOrNull(ps, 4, entity.getAccountId());
        PreparedStatementHelper.setLongOrNull(ps, 5, entity.getUserId());
        return ps;
    }

    @Override
    protected PreparedStatement createUpdateStatement(Connection connection, Card entity) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE card SET card_number=?, card_type=?, card_date=?, account_id=?, user_id=?   WHERE card_id=?;");
        ps.setString(1, entity.getCardNumber());
        ps.setString(2, entity.getCardType());
        PreparedStatementHelper.setDate(ps, 3, entity.getCardDate());
        PreparedStatementHelper.setLongOrNull(ps, 4, entity.getAccountId());
        PreparedStatementHelper.setLongOrNull(ps, 5, entity.getUserId());
        PreparedStatementHelper.setLongOrNull(ps, 6, entity.getCardId());
        return ps;
    }

    @Override
    protected void extractNewKeyBySaving(PreparedStatement ps, Card entity) throws SQLException {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                entity.setCardId(generatedKeys.getLong(1));
            }
            else {
                throw new SQLException("Creating card failed, no ID obtained.");
            }
        }
    }
}
