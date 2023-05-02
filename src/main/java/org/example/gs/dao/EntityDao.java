package org.example.gs.dao;

import org.example.gs.model.Parameters;

import java.util.Collection;

public interface EntityDao<T> {
    Collection<T> getAll(Parameters parameters);
    long insert(T t);
    void delete(long id);
    T getById(long id);
    T getByName(String name);
}