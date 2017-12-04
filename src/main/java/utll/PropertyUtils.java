package utll;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by computer on 19.11.2017.
 */
public class PropertyUtils {

    public static final String JDBC_DRIVER = "jdbc.driver";
    public static final String JDBC_DB_URL = "jdbc.url";
    public static final String JDBC_USER = "jdbc.username";
    public static final String JDBC_PASS = "jdbc.password";
    public static final String JDBC_MIN_IDLE = "jdbc.minIdle";
    public static final String JDBC_MAX_IDLE = "jdbc.maxIdle";
    public static final String JDBC_MAX_OPEN_PREPARDSTATEMENTS = "jdbc.maxOpenPreparedStatements";
    private static final String PROPERTY_FILE = "application.properties";

    public static String getProperyByKey(String key) {
        try(InputStream inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream(PROPERTY_FILE)) {
            Properties props = new Properties();
            props.load(inputStream);
            return props.getProperty(key);
        } catch (IOException e) {
            //TODO add logger
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

}
