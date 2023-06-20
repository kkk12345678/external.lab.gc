package org.example.gc.dto;

import lombok.Data;
import org.example.gc.entity.Order;
import org.springframework.stereotype.Component;

@Component
@Data
public class OrderDto implements EntityDto<Order> {
    private Long userId;
    private Long giftCertificateId;

    @Override
    public Order toEntity() {
        return new Order();
    }
}