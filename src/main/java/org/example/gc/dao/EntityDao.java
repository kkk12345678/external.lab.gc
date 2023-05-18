package org.example.gc.dao;

import java.util.Collection;

public interface EntityDao<T> {
    Collection<T> getAll(ParametersHandler parametersHandler);
    long insert(T t);
    void delete(long id);
    T getById(long id);
    T getByName(String name);
}