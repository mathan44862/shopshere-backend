package com.example.shopspherebackend.dto;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String phone
) {
}
