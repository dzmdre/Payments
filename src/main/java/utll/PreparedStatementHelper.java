package utll;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

/**
 * Created by computer on 26.11.2017.
 */
public class PreparedStatementHelper {

    public static void setLongOrNull(PreparedStatement statement, int column, Long value) throws SQLException {
        if (value != null) {
            statement.setLong(column, value);
        } else {
            statement.setNull(column, Types.BIGINT);
        }
    }

    public static void setBooleanOrNull(PreparedStatement statement, int column, Boolean value) throws SQLException {
        if (value != null) {
            statement.setBoolean(column, value);
        } else {
            statement.setNull(column, Types.BOOLEAN);
        }
    }

    public static void setDate(PreparedStatement statement, int column, LocalDate value) throws SQLException {
        statement.setDate(column, value != null ? Date.valueOf(value) : null);
    }
}
