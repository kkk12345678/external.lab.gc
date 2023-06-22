package org.example.gc.it.service;

import org.example.gc.Application;
import org.example.gc.dto.OrderDto;
import org.example.gc.entity.GiftCertificate;
import org.example.gc.entity.Order;
import org.example.gc.entity.User;
import org.example.gc.parameters.GiftCertificateParameters;
import org.example.gc.parameters.UserParameters;
import org.example.gc.service.GiftCertificateService;
import org.example.gc.service.OrderService;
import org.example.gc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private GiftCertificateService giftCertificateService;

    @Test
    @Transactional
    void testInsertNewOrderAndDeleteWhenDataIsValid() {
        UserParameters userParameters = new UserParameters();
        userParameters.setLimit(1);
        GiftCertificateParameters giftCertificateParameters = new GiftCertificateParameters();
        giftCertificateParameters.setLimit(1);
        List<User> users = userService.getAll(new UserParameters());
        List<GiftCertificate> giftCertificates = giftCertificateService.getAll(giftCertificateParameters);
        if (users.size() > 0 && giftCertificates.size() > 0) {
            Instant now = Instant.now();
            Order order = orderService.add(new OrderDto(users.get(0).getId(), giftCertificates.get(0).getId()));
            Long orderId = order.getId();
            assertNotNull(order);
            System.out.println(now);
            System.out.println(order.getCreateDate());
            assertTrue(order.getCreateDate().compareTo(now) >= 0);
            assertEquals(giftCertificates.get(0), order.getGiftCertificate());
            assertEquals(users.get(0), order.getUser());
            orderService.remove(orderId);
            assertNull(orderService.getById(orderId));
        }
    }

    @Test
    void testInsertNewOrderWhenUserDoesNotExist() {
        UserParameters userParameters = new UserParameters();
        userParameters.setLimit(1);
        List<User> users = userService.getAll(new UserParameters());
        if (users.size() > 0) {
            assertThrows(IllegalArgumentException.class,
                    () -> orderService.add(new OrderDto(users.get(0).getId(), -1L)));
        }
    }

    @Test
    void testInsertNewOrderWhenGiftCertificateDoesNotExist() {
        GiftCertificateParameters giftCertificateParameters = new GiftCertificateParameters();
        giftCertificateParameters.setLimit(1);
        List<GiftCertificate> giftCertificates = giftCertificateService.getAll(giftCertificateParameters);
        if (giftCertificates.size() > 0) {
            assertThrows(IllegalArgumentException.class,
                    () -> orderService.add(new OrderDto(-1L, giftCertificates.get(0).getId())));
        }
    }
}