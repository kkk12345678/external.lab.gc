package org.example.gc.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotEmpty(message = "Gift certificate parameter 'name' must not be empty.")
    private String name;
    private Collection<TagRequestDto> tags;
    private String description;

    public static GiftCertificate fromDtoToEntity(GiftCertificateRequestInsertDto giftCertificateRequestInsertDto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(giftCertificateRequestInsertDto.getName());
        giftCertificate.setDescription(giftCertificateRequestInsertDto.getDescription());
        giftCertificate.setPrice(giftCertificateRequestInsertDto.getPrice());
        giftCertificate.setDuration(giftCertificateRequestInsertDto.getDuration());
        return giftCertificate;
    }
}