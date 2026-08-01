package com.example.shopspherebackend.dto;

import com.example.shopspherebackend.entity.Role;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String phone,
        Role role,
        Boolean enabled,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt
) {
}
