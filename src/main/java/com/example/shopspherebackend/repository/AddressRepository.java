package com.example.shopspherebackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopspherebackend.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByIsDeletedFalse();

    List<Address> findByUserIdAndIsDeletedFalse(Long userId);

    Optional<Address> findByIdAndIsDeletedFalse(Long id);
}
