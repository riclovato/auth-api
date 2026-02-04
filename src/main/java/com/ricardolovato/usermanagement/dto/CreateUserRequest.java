package com.ricardolovato.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
                @NotBlank(message = "Username is required") String username,
                @NotBlank(message = "Full name is required") String fullName,
                @Email(message = "Email should be valid") @NotBlank(message = "Email is required") String email,
                @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters long") String password) {
}