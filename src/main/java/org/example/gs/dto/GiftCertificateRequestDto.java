package org.example.gs.dto;

import lombok.Data;
import org.example.gs.model.GiftCertificate;

import java.util.Collection;

@Data
public class GiftCertificateRequestDto {
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private Collection<TagRequestDto> tags;

    public static GiftCertificate fromDtoToEntity(GiftCertificateRequestDto giftCertificateRequestDto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(giftCertificateRequestDto.getName());
        giftCertificate.setDescription(giftCertificateRequestDto.getDescription());
        giftCertificate.setPrice(giftCertificateRequestDto.getPrice());
        giftCertificate.setDuration(giftCertificateRequestDto.getDuration());
        return giftCertificate;
    }
}