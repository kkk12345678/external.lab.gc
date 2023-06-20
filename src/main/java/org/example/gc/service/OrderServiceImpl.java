package org.example.gc.service;

import org.example.gc.dto.OrderDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.entity.Order;
import org.example.gc.entity.User;
import org.example.gc.parameters.OrderParameters;
import org.example.gc.repository.GiftCertificateRepository;
import org.example.gc.repository.OrderRepository;
import org.example.gc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String ERROR_NO_SUCH_USER = "There is no user with 'id' = '%d'.";
    private static final String ERROR_NO_SUCH_GIFT_CERTIFICATE = "There is no gift certificate with 'id' = '%d'.";
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Override
    public List<Order> getAll(OrderParameters orderParameters) {
        //TODO parameters
        return orderRepository.getAll(orderParameters);
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
            throw new IllegalArgumentException(String.format(ERROR_NO_SUCH_USER, userId));
        }
        Long giftCertificateId = dto.getGiftCertificateId();
        GiftCertificate giftCertificate = giftCertificateRepository.getById(giftCertificateId);
        if (giftCertificate == null) {
            throw new IllegalArgumentException(String.format(ERROR_NO_SUCH_GIFT_CERTIFICATE, giftCertificateId));
        }
        Order order = dto.toEntity();
        order.setUser(user);
        order.setGiftCertificate(giftCertificate);
        order.setSum(giftCertificate.getPrice());
        order.setCreateDate(Instant.now());
        return orderRepository.insertOrUpdate(order);
    }
}
