package org.example.gc.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class GiftCertificateRequestUpdateDto {
    @Positive(message = "Gift certificate parameter 'price' must be positive double.")
    private Double price;
    @Positive(message = "Gift certificate parameter 'duration' must be positive integer.")
    private Integer duration;
    private String name;
    private Collection<TagRequestDto> tags;
    private String description;
}
