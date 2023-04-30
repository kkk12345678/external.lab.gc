package org.example.gs.dao;

import org.example.gs.model.Parameters;

import java.util.List;

public interface EntityDao<T> {
    List<T> getAll(Parameters parameters);
    long insert(T t);
    void delete(long id);
    T getById(long id);
    T getByName(String name);
}
