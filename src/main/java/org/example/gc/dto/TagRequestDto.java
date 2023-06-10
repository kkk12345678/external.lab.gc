package org.example.gc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.gc.entity.Tag;

@Data
public class TagRequestDto {
    @NotBlank(message="Tag parameter 'name' must not be empty.")
    @Size(max = 256, message = "Tag parameter 'name' must not contain more than 256 characters.")
    private String name;

    public Tag toEntity() {
        return new Tag(null, name);
    }
}