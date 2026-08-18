package com.example.shopspherebackend.dto;

public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        long expiresIn,
        UserResponseDTO user) {
}
