package Dao;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by computer on 13.11.2017.
 */
public interface CRUD<T> {
    T getByid(int id);
    List<T> getAll();
    void save(T entity);
    void delete(T entity);
    T extract(ResultSet result);
}
