package com.example.shopspherebackend.dto;

import com.example.shopspherebackend.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
                @NotBlank(message = "Name is required") @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") String name,
                @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,
                @NotBlank(message = "Password is required") @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters") String password,
                @NotBlank(message = "Phone number is required") @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number") String phone,
                Role role) {
}
