package org.example.gc.dto;

import org.example.gc.entity.GiftCertificate;

import java.util.Set;

public interface GiftCertificateDto extends EntityDto<GiftCertificate> {
    Set<TagDto> getTags();
}
