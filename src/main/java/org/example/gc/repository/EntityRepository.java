package org.example.gc.repository;

import jakarta.persistence.Query;
import org.example.gc.parameters.Parameters;

public interface EntityRepository<T> {
    String FIELD_NAME = "name";
    String FIELD_ID = "id";

    T insertOrUpdate(T t);
    void delete(T t);
    T getById(Long id);
    T getByName(String name);

    default void setPagination(Query query, Parameters parameters) {
        Integer limit = parameters.getLimit();
        if (limit != null) {
            query.setMaxResults(limit);
            Integer page = parameters.getPage();
            query.setFirstResult(page == null ? 0 : (page - 1) * limit);
        }
    }
}