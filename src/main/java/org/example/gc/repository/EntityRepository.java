package org.example.gc.repository;

import org.example.gc.entity.Parameters;

import java.util.List;

public interface EntityRepository<T> {
    String FIELD_NAME = "name";
    String FIELD_ID = "id";

    List<T> getAll(Parameters parameters);
    T insertOrUpdate(T t);
    void delete(T t);
    T getById(Long id);
    T getByName(String name);
}
