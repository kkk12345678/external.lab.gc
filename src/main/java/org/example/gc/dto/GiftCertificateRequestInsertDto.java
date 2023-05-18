package org.example.gc.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.gc.model.GiftCertificate;

import java.util.Collection;

@Getter
@Setter
public class GiftCertificateRequestInsertDto {
    @Positive(message = "Gift certificate parameter 'price' must be positive double.")
    @NotNull(message = "Gift certificate parameter 'price' must not be empty.")
    private Double price;
    @Positive(message = "Gift certificate parameter 'duration' must be positive integer.")
    @NotNull(message = "Gift certificate parameter 'duration' must not be empty.")
    private Integer duration;
    @NotBlank(message = "Gift certificate parameter 'name' must not be empty.")
    @Size(max = 256, message = "Gift certificate parameter 'name' must not contain more than 256 characters.")
    private String name;
    private Collection<TagRequestDto> tags;
    private String description;

    public GiftCertificate toEntity() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        return giftCertificate;
    }
}