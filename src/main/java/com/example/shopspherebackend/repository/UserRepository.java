package com.example.shopspherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopspherebackend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
