package org.example.gs.model;

import lombok.*;

@Data
public class Tag {
    private long id;
    private String name;

    public static Tag create(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
