package org.example.gc.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.example.gc.entity.GiftCertificate;

import java.util.Set;

@Getter
@Setter
public class GiftCertificateUpdateDto implements GiftCertificateDto {
    @Positive(message = "Gift certificate parameter 'price' must be positive double.")
    private Double price;
    @Positive(message = "Gift certificate parameter 'duration' must be positive integer.")
    private Integer duration;
    private String name;
    private Set<TagDto> tags;
    private String description;

    @Override
    public GiftCertificate toEntity() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        return giftCertificate;
    }
}
