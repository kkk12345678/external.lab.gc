package org.example.gc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gc.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements EntityDto<Role> {
    @NotBlank(message = "Role parameter 'name' must not be empty.")
    @Size(max = 256, message = "Role parameter 'name' must not contain more than 256 characters.")
    private String name;

    @Override
    public Role toEntity() {
        return new Role(name);
    }
}
