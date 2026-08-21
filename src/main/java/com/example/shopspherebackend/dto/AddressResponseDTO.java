package com.example.shopspherebackend.dto;

import com.example.shopspherebackend.entity.AddressType;

public record AddressResponseDTO(
        Long id,
        String fullName,
        String phone,
        String address_line1,
        String address_line2,
        String city,
        String state,
        String postal_code,
        String country,
        AddressType type,
        boolean isDefault,
        java.time.LocalDateTime createdAt,
        java.time.LocalDateTime updatedAt) {
}
