package org.example.gc.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.gc.entity.GiftCertificate;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateUpdateDto implements GiftCertificateDto {
    private String name;
    private String description;
    @Positive(message = "Gift certificate parameter 'price' must be positive double.")
    private Double price;
    @Positive(message = "Gift certificate parameter 'duration' must be positive integer.")
    private Integer duration;

    private Set<TagDto> tags;


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
