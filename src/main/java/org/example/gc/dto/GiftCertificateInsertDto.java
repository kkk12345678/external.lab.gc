package org.example.gc.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.gc.entity.GiftCertificate;

import java.util.Set;

@Getter
@Setter
@ToString
public class GiftCertificateInsertDto implements GiftCertificateDto {
    @Positive(message = "Gift certificate parameter 'price' must be positive double.")
    @NotNull(message = "Gift certificate parameter 'price' must not be empty.")
    private Double price;
    @Positive(message = "Gift certificate parameter 'duration' must be positive integer.")
    @NotNull(message = "Gift certificate parameter 'duration' must not be empty.")
    private Integer duration;
    @NotBlank(message = "Gift certificate parameter 'name' must not be empty.")
    @Size(max = 256, message = "Gift certificate parameter 'name' must not contain more than 256 characters.")
    private String name;
    private Set<TagDto> tags;
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