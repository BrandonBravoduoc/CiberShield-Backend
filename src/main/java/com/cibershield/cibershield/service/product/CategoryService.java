package com.cibershield.cibershield.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.model.product.Category;

import com.cibershield.cibershield.repository.product.CategoryRespository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRespository categoryRespository;

    public List<Category> searchAll() {
        return categoryRespository.findAll();
    }

    public Category searchById(Long id) {
        Category category = categoryRespository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Categoría no encontrada");
        }
        return category;
    }

    public Category searchByName(String name) {
        Category category = categoryRespository.findByCategoryName(name)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return category;
    }

    public Category saveCategory(Category category) {
        if (category == null) {
            throw new RuntimeException("La categoría no puede estar nula");
        }
        if (category.getCategoryName() == null || category.getCategoryName().trim().isBlank()) {
            throw new RuntimeException("El nombre de la categoría no puede estar vacío.");
        }
        String standardized = category.getCategoryName().trim().toLowerCase();
        return categoryRespository.findByCategoryName(standardized).orElseGet(() -> {
            Category newCategory = new Category();
            newCategory.setCategoryName(category.getCategoryName().trim());
            return categoryRespository.save(newCategory);
        });

    }

    public void deleteCategory(Long id) {
        Category category = categoryRespository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
        categoryRespository.deleteById(id);
    }
}
