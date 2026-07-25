package com.example.shopspherebackend.dto;

public record ProductResponseDTO(
    Long id,
    String name,
    String description,
    Double price,
    Long stockQuantity,
    String imageUrl
) {
}
