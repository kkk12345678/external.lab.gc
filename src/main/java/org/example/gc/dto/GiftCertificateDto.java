package org.example.gc.dto;

import java.util.Set;

public interface GiftCertificateDto extends EntityDto {
    Set<TagDto> getTags();
}
