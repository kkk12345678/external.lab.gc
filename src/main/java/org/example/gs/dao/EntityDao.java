package org.example.gs.dao;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public interface EntityDao<T> {
    void setDataSource(DataSource dataSource);
    void insert(T t);
    void delete(long id);
    void update(T t);
    Optional<T> getById(long id);
    Optional<T> getByName(String name);
    List<T> getAll();
}
