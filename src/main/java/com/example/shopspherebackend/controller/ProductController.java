package com.example.shopspherebackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopspherebackend.dto.ProductResponseDTO;
import com.example.shopspherebackend.dto.ProductRequestDTO;
import com.example.shopspherebackend.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        return productService.createProduct(productRequestDTO);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        return productService.updateProduct(id, productRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }
}
