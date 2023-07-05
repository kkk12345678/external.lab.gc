package org.example.gc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.gc.entity.Role;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private Role role;
}
