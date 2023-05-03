package org.example.gs.dto;

import lombok.Data;
import org.example.gs.model.GiftCertificate;
import org.example.gs.model.Tag;

import java.util.Collection;

@Data
public class GiftCertificateResponseDto {
    private long id;
    private String name;
    private String description;
    private double price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private Collection<Tag> tags;

    public static GiftCertificateResponseDto fromEntityToDto(GiftCertificate giftCertificate) {
        GiftCertificateResponseDto giftCertificateResponseDto = new GiftCertificateResponseDto();
        giftCertificateResponseDto.setId(giftCertificate.getId());
        giftCertificateResponseDto.setName(giftCertificate.getName());
        giftCertificateResponseDto.setDescription(giftCertificate.getDescription());
        giftCertificateResponseDto.setPrice(giftCertificate.getPrice());
        giftCertificateResponseDto.setDuration(giftCertificate.getDuration());
        giftCertificateResponseDto.setCreateDate(giftCertificate.getCreateDate());
        giftCertificateResponseDto.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        giftCertificateResponseDto.setTags(giftCertificate.getTags());
        return giftCertificateResponseDto;
    }
}