package com.example.shopspherebackend.controller;

import com.example.shopspherebackend.dto.UserRequestDTO;
import com.example.shopspherebackend.dto.UserResponseDTO;
import com.example.shopspherebackend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO request) {
        return service.createUser(request);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        return service.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO request) {
        return service.updateUser(id, request);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserbyID(@PathVariable Long id) {
        return service.getUserByID(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserbyId(@PathVariable Long id) {
        service.deleteUserByID(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
