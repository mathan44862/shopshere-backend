package com.example.shopspherebackend.dto;

import com.example.shopspherebackend.entity.AddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
        @NotBlank(message = "Name is required") @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") String fullName,
        @NotBlank(message = "Phone number is required") @Size(min = 3, max = 20, message = "Phone number must be between 3 and 20 characters") String phone,
        @NotBlank(message = "Address Line 1 is required") @Size(min = 3, max = 255, message = "Address Line 1 must be between 3 and 255 characters") String address_line1,
        @NotBlank(message = "Address Line 2 is required") @Size(min = 3, max = 255, message = "Address Line 2 must be between 3 and 255 characters") String address_line2,
        @NotBlank(message = "City is required") @Size(min = 3, max = 100, message = "City must be between 3 and 100 characters") String city,
        @NotBlank(message = "State is required") @Size(min = 3, max = 100, message = "State must be between 3 and 100 characters") String state,
        @NotBlank(message = "Postal code is required") @Size(min = 3, max = 20, message = "Postal code must be between 3 and 20 characters") String postal_code,
        @NotBlank(message = "Country is required") @Size(min = 3, max = 255, message = "Country must be between 3 and 255 characters") String country,
        AddressType type,
        Boolean isDefault) {

    public AddressRequestDTO {
        if (isDefault == null) {
            isDefault = false;
        }
    }
}
