package org.example.gc.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Data
@Component
public class GiftCertificate {
    private long id;
    private String name;
    private String description;
    private double price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    private Collection<Tag> tags;

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void addTag(long tagId, String tagName) {
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setName(tagName);
        addTag(tag);
    }
}