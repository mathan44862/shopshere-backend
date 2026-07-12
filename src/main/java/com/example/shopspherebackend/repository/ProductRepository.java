package com.example.shopspherebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopspherebackend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
