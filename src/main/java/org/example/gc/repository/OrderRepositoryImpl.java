package org.example.gc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.gc.entity.Order;
import org.example.gc.parameters.OrderParameters;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository, Serializable {
    private static final String JPQL_ALL = "from Order";

    @PersistenceContext
    private transient EntityManager entityManager;

    @Override
    public Order insertOrUpdate(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public void delete(Order order) {
        entityManager.remove(order);
    }

    @Override
    public Order getById(Long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public Order getByName(String name) {
        return null;
    }

    @Override
    public List<Order> getAll(OrderParameters parameters) {
        TypedQuery<Order> query = entityManager.createQuery(JPQL_ALL, Order.class);
        setPagination(query, parameters);
        return query.getResultList();
    }
}
