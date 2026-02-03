package com.ricardolovato.usermanagement.dto;
import java.time.LocalDateTime;


public record UserResponse(Long id, String email, LocalDateTime createdAt) {
    
}
