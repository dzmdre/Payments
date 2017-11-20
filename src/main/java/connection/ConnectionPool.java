package connection;

import org.apache.commons.dbcp.BasicDataSource;
import utll.PropertyUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by computer on 18.11.2017.
 */
public class ConnectionPool {


    private static BasicDataSource dataSource;


    private ConnectionPool(){
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(PropertyUtils.getProperyByKey(PropertyUtils.JDBC_DRIVER));
        dataSource.setUsername(PropertyUtils.getProperyByKey(PropertyUtils.JDBC_USER));
        dataSource.setPassword(PropertyUtils.getProperyByKey(PropertyUtils.JDBC_PASS));
        dataSource.setUrl(PropertyUtils.getProperyByKey(PropertyUtils.JDBC_DB_URL));

        dataSource.setMinIdle(Integer.parseInt(PropertyUtils.getProperyByKey(PropertyUtils.JDBC_MIN_IDLE)));
        dataSource.setMaxIdle(Integer.parseInt(PropertyUtils.getProperyByKey(PropertyUtils.JDBC_MAX_IDLE)));
        dataSource.setMaxOpenPreparedStatements(Integer.parseInt(PropertyUtils.getProperyByKey(PropertyUtils.JDBC_MAX_OPEN_PREPARDSTATEMENTS)));
    }

    public static synchronized DataSource getInstance() {
        if (dataSource == null) {
            ConnectionPool connectionPool = new ConnectionPool();
            return dataSource;
        } else {
            return dataSource;
        }

    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
