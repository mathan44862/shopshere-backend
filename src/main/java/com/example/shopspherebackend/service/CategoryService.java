package com.example.shopspherebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shopspherebackend.dto.CategoryRequestDTO;
import com.example.shopspherebackend.dto.CategoryResponseDTO;
import com.example.shopspherebackend.entity.Category;
import com.example.shopspherebackend.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());

        return toResponseDTO(repository.save(category));
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(request.name());
        category.setDescription(request.description());

        return toResponseDTO(repository.save(category));
    }

    public void deleteCategory(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
