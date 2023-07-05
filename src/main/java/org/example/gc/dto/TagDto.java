package org.example.gc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gc.entity.Tag;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto implements EntityDto<Tag> {
    @NotBlank(message="Tag parameter 'name' must not be empty.")
    @Size(max = 256, message = "Tag parameter 'name' must not contain more than 256 characters.")
    private String name;
}