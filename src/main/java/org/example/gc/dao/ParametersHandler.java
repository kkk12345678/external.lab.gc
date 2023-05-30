package org.example.gc.dao;

public interface ParametersHandler {
    default String whereClause() {
        return "";
    }
    default String orderClause() {
        return "";
    }
    default String limitClause() {
        return "";
    }
}