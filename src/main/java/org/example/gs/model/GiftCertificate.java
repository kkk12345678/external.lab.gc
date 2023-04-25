package org.example.gs.model;

import lombok.*;

import java.util.List;

@Data
public class GiftCertificate {
    private long id;
    private String name;
    private String description;
    private double price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private List<Tag> tags;

    public void addTag(long id, String name) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setId(id);
        tags.add(tag);
    }
}