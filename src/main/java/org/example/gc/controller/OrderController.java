package org.example.gc.controller;

import org.example.gc.dto.OrderDto;
import org.example.gc.entity.Order;
import org.example.gc.parameters.OrderParameters;
import org.example.gc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final String ORDER_ID = "orderId";
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders(OrderParameters orderParameters) {
        return orderService.getAll(orderParameters);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable(ORDER_ID) Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order addOrder(@RequestBody(required = false) OrderDto dto) {
        return orderService.add(dto);
    }
}