package org.example.gc.service;

import org.example.gc.dto.OrderDto;
import org.example.gc.entity.Order;
import org.example.gc.parameters.OrderParameters;

import java.util.List;

public interface OrderService {
    List<Order> getAll(OrderParameters orderParameters);
    Order getById(Long id);
    Order add(OrderDto dto);
}
