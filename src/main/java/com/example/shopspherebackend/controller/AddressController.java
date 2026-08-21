package com.example.shopspherebackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopspherebackend.dto.AddressRequestDTO;
import com.example.shopspherebackend.dto.AddressResponseDTO;
import com.example.shopspherebackend.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<AddressResponseDTO> getAddressesByUser(@PathVariable Long userId) {
        return addressService.getAddressByUser(userId);
    }

    @GetMapping("/{id}")
    public AddressResponseDTO getAddressByUserAndId(@PathVariable Long userId, @PathVariable Long id) {
        return addressService.getAddressByUserAndId(userId, id);
    }

    @PostMapping
    public AddressResponseDTO createAddress(
            @PathVariable Long userId,
            @Valid @RequestBody AddressRequestDTO request) {
        return addressService.createAddress(userId, request);
    }

    @PutMapping("/{id}")
    public AddressResponseDTO updateAddress(
            @PathVariable Long userId,
            @PathVariable Long id,
            @Valid @RequestBody AddressRequestDTO request) {
        return addressService.updateAddress(userId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long userId, @PathVariable Long id) {
        addressService.deleteAddress(userId, id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}
