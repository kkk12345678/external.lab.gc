package org.example.gc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.gc.entity.Tag;

import java.util.Set;

@Data
@AllArgsConstructor
public class GiftCertificateResponseDto {
    private long id;
    private String name;
    private String description;
    private double price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private Set<Tag> tags;
}