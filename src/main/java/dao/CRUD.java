package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by computer on 13.11.2017.
 */
public interface CRUD<T> {
    T getByid(Long id);
    List<T> getAll();
    T save(T entity);
    T delete(T entity);
    T extract(ResultSet result) throws SQLException;
}
