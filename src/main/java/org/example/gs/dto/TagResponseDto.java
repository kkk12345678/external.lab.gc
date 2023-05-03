package org.example.gs.dto;

import lombok.Data;
import org.example.gs.model.Tag;

@Data
public class TagResponseDto {
    private long id;
    private String name;

    public static TagResponseDto fromEntityToDto(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagResponseDto tagResponseDto = new TagResponseDto();
        tagResponseDto.setId(tag.getId());
        tagResponseDto.setName(tag.getName());
        return tagResponseDto;
    }
}