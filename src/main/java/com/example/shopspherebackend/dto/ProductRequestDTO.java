package com.example.shopspherebackend.dto;

public record ProductRequestDTO(
    String name,
    String description,
    Double price,
    Long stockQuantity,
    String imageUrl
) {
}