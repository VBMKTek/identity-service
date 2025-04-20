package com.preschool.identityservice.api.dto.request;

import com.preschool.libraries.base.annotation.SensitiveData;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @SensitiveData
    private String password;
}
