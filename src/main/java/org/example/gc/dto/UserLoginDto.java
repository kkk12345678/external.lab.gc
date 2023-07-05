package org.example.gc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.gc.entity.User;

@Data
public class UserLoginDto implements EntityDto<User> {
    @NotBlank(message="User parameter 'name' must not be empty.")
    @Size(max = 256, message = "User parameter 'name' must not contain more than 256 characters.")
    String name;
    @NotBlank(message="User parameter 'password' must not be empty.")
    @Size(max = 256, message = "User parameter 'password' must not contain more than 16 characters.")
    String password;
}