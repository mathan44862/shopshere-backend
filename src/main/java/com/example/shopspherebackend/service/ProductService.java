package com.example.shopspherebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shopspherebackend.dto.ProductRequestDTO;
import com.example.shopspherebackend.dto.ProductResponseDTO;
import com.example.shopspherebackend.entity.Product;
import com.example.shopspherebackend.repository.ProductRepository;

@Service
public class ProductService {
    ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        var product = new Product();
        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
        product.setPrice(productRequestDTO.price());
        product.setStockQuantity(productRequestDTO.stockQuantity());
        product.setImageUrl(productRequestDTO.imageUrl());

        return toResponseDTO(productRepository.save(product));
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }  

    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStockQuantity(),
                        product.getImageUrl()))
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
        product.setPrice(productRequestDTO.price());
        product.setStockQuantity(productRequestDTO.stockQuantity());
        product.setImageUrl(productRequestDTO.imageUrl());

        return toResponseDTO(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl());
    }
}
