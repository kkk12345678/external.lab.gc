package org.example.gc.parameters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderParameters extends Parameters {
    private Long userId;
    private Long giftCertificateId;
}