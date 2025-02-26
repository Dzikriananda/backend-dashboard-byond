package com.dzikriananda.multimatic_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    @NotEmpty(message = "Username should not be empty")
    private String username; // Required for register, optional for login

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email; // Required for register, optional for login

    @NotEmpty(message = "Password should not be empty")
    private String password; // Always required
}