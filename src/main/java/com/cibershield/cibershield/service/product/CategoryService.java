package com.cibershield.cibershield.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.productsDTO.CategoryDTO;
import com.cibershield.cibershield.model.product.Category;
import com.cibershield.cibershield.repository.product.CategoryRespository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRespository categoryRepository;

    public List<CategoryDTO.Response> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoryDTO.Response findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return mapToResponse(category);
    }

    public CategoryDTO.Response searchByName(String name) {
        Category category = categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return mapToResponse(category);
    }

    public CategoryDTO.Response updateCategory(Long id, CategoryDTO.Update dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        if (dto.categoryName() != null && !dto.categoryName().isBlank()) {
            String standardized = dto.categoryName().trim().toUpperCase();
            if (categoryRepository.existsByCategoryName(standardized) &&
                    !category.getCategoryName().equals(standardized)) {
                throw new RuntimeException("El nombre de la categoría ya existe.");
            }

            category.setCategoryName(standardized);
        }

        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    public CategoryDTO.Response saveCategory(CategoryDTO.Create dto) {
        if (dto == null) {
            throw new IllegalArgumentException("La categoría no puede estar nula");
        }
        if (dto.categoryName() == null || dto.categoryName().trim().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }

        String standardized = dto.categoryName().trim().toUpperCase();

        if (categoryRepository.existsByCategoryName(standardized)) {
            throw new RuntimeException("El nombre de la categoría ya existe");
        }

        Category category = new Category();
        category.setCategoryName(standardized);

        category = categoryRepository.save(category);

        return mapToResponse(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada.");
        }
        categoryRepository.deleteById(id);
    }

    private CategoryDTO.Response mapToResponse(Category c) {
        return new CategoryDTO.Response(
                c.getId(),
                c.getCategoryName());
    }
}