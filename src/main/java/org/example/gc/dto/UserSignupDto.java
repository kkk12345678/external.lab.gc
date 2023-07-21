package org.example.gc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserSignupDto extends UserLoginDto {
    public UserSignupDto(
            @NotBlank(message = "User parameter 'name' must not be empty.")
            @Size(max = 256, message = "User parameter 'name' must not contain more than 256 characters.")
            String name,

            @NotBlank(message = "User parameter 'password' must not be empty.")
            @Size(max = 256, message = "User parameter 'password' must not contain more than 16 characters.")
            String password) {
        super(name, password);
    }
}