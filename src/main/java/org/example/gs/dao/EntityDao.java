package org.example.gs.dao;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T> {
    List<T> getAll();
    long insert(T t);
    void delete(long id);
    void update(T t);
    Optional<T> getById(long id);

}
