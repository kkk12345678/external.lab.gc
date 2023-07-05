package org.example.gc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.OrderDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.entity.Order;
import org.example.gc.entity.User;
import org.example.gc.exception.NoSuchUserException;
import org.example.gc.parameters.OrderParameters;
import org.example.gc.repository.GiftCertificateRepository;
import org.example.gc.repository.OrderRepository;
import org.example.gc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private static final String ERROR_NO_SUCH_USER = "There is no user with 'id' = '%d'.";
    private static final String ERROR_NO_SUCH_GIFT_CERTIFICATE = "There is no gift certificate with 'id' = '%d'.";
    private static final String ERROR_ID_NOT_FOUND =
            "There is no order with 'id' = '%d'.";
    private static final String MESSAGE_ORDERS_FOUND =
            "%d orders were successfully found.";
    private static final String MESSAGE_NO_ORDER_BY_ID_FOUND =
            "No order with 'id' = '%d' was found.";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Override
    public List<Order> getAll(OrderParameters orderParameters) {
        List<Order> orders = orderRepository.getAll(orderParameters);
        log.info(String.format(MESSAGE_ORDERS_FOUND, orders.size()));
        return orders;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.getById(id);
    }

    @Override
    @Transactional
    public Order add(OrderDto dto) {
        Long userId = dto.getUserId();
        User user = userRepository.getById(dto.getUserId());
        if (user == null) {
            throw new NoSuchUserException(String.format(ERROR_NO_SUCH_USER, userId));
        }
        Long giftCertificateId = dto.getGiftCertificateId();
        GiftCertificate giftCertificate = giftCertificateRepository.getById(giftCertificateId);
        if (giftCertificate == null) {
            throw new NoSuchElementException(String.format(ERROR_NO_SUCH_GIFT_CERTIFICATE, giftCertificateId));
        }
        Order order = new Order();
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        order.setSum(giftCertificate.getPrice());
        order.setCreateDate(Instant.now());
        return orderRepository.insertOrUpdate(order);
    }

    @Override
    public void remove(Long id) {
        Order order = check(id);
        orderRepository.delete(order);
    }

    private Order check(Long id) {
        Order order = orderRepository.getById(id);
        if (order == null) {
            log.info(String.format(MESSAGE_NO_ORDER_BY_ID_FOUND, id));
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        return order;
    }
}