package org.example.gs.model;

import lombok.*;

@Data
public class GiftCertificate {
    private long id;
    private String name;
    private String description;
    private double price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private String[] tags;
}