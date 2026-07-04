package com.example.shopspherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopspherebackend.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}