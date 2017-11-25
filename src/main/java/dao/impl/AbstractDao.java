package dao.impl;

import model.PaymentUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by computer on 25.11.2017.
 */
public abstract class AbstractDao<T> {
    protected abstract PreparedStatement createSaveStatement(Connection connection, T entity) throws SQLException ;
    protected abstract PreparedStatement createUpdateStatement(Connection connection, T entity) throws SQLException;
    protected abstract void extractNewKeyBySaving(PreparedStatement ps, T entity) throws SQLException;
}
