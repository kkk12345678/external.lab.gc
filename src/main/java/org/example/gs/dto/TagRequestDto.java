package org.example.gs.dto;

import lombok.Data;
import org.example.gs.model.Tag;

@Data
public class TagRequestDto {
    private String name;

    public static Tag fromDtoToEntity(TagRequestDto tagRequestDto) {
        Tag tag = new Tag();
        tag.setName(tagRequestDto.getName());
        return tag;
    }
}