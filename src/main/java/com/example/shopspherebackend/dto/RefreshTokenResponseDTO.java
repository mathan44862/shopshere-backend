package com.example.shopspherebackend.dto;

public record RefreshTokenResponseDTO(
        String accessToken,
        String tokenType,
        long expiresIn) {
}
