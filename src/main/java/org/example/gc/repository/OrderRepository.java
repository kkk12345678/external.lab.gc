package org.example.gc.repository;

import org.example.gc.entity.Order;
import org.example.gc.parameters.OrderParameters;

import java.util.List;

public interface OrderRepository extends EntityRepository<Order> {
    List<Order> getAll(OrderParameters parameters);
}
