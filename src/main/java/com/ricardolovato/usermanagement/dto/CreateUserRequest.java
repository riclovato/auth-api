package com.ricardolovato.usermanagement.dto;

public record CreateUserRequest(
        String username,
        String fullName,
        String email,
        String password
) {}