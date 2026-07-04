package com.example.shopspherebackend.dto;

public record UserRequestDTO(
        String name,
        String email,
        String password,
        String phone
) {
}
