package org.example.gc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.gc.entity.Order;

@Data
@AllArgsConstructor
public class OrderDto implements EntityDto<Order> {
    private Long userId;
    private Long giftCertificateId;

    @Override
    public Order toEntity() {
        return new Order();
    }
}