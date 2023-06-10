package org.example.gc.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GiftCertificateRequestUpdateDto implements GiftCertificateRequestDto {
    @Positive(message = "Gift certificate parameter 'price' must be positive double.")
    private Double price;
    @Positive(message = "Gift certificate parameter 'duration' must be positive integer.")
    private Integer duration;
    private String name;
    private Set<TagRequestDto> tags;
    private String description;
}
