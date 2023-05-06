package org.example.gc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.gc.model.Tag;

@Data
public class TagRequestDto {
    @NotBlank(message="Tag parameter 'name' must not be empty.")
    private String name;

    public static Tag fromDtoToEntity(TagRequestDto tagRequestDto) {
        Tag tag = new Tag();
        tag.setName(tagRequestDto.getName());
        return tag;
    }
}