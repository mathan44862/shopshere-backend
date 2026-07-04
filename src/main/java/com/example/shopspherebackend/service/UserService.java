package com.example.shopspherebackend.service;

import com.example.shopspherebackend.dto.UserRequestDTO;
import com.example.shopspherebackend.dto.UserResponseDTO;
import com.example.shopspherebackend.entity.User;
import com.example.shopspherebackend.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setPhone(request.phone());

        return toResponseDTO(repository.save(user));
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }
}
