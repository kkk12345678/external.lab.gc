package org.example.gc.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.gc.entity.GiftCertificate;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateInsertDto implements GiftCertificateDto {
    @NotBlank(message = "Gift certificate parameter 'name' must not be empty.")
    @Size(max = 256, message = "Gift certificate parameter 'name' must not contain more than 256 characters.")
    private String name;
    private String description;
    @Positive(message = "Gift certificate parameter 'price' must be positive double.")
    @NotNull(message = "Gift certificate parameter 'price' must not be empty.")
    private Double price;
    @Positive(message = "Gift certificate parameter 'duration' must be positive integer.")
    @NotNull(message = "Gift certificate parameter 'duration' must not be empty.")
    private Integer duration;
    private Set<TagDto> tags;

    public GiftCertificate toEntity() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        return giftCertificate;
    }
}