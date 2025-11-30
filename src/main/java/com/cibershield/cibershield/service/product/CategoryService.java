package com.cibershield.cibershield.service.product;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.productDTO.CategoryDTO;
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

    public CategoryDTO.Response saveCategory(CategoryDTO.Create dto, Authentication authentication) {
        if (dto == null) {
            throw new RuntimeException("La categoría no puede estar nula");
        }
        if (dto.categoryName() == null || dto.categoryName().trim().isBlank()) {
            throw new RuntimeException("El nombre de la categoría no puede estar vacío.");
        }
        String standardized = dto.categoryName().trim().toUpperCase();
        if (categoryRespository.existsByCategoryName(standardized)) {
            throw new RuntimeException("El nombre de la categoria ya existe");
        }

        Category category = new Category(); 
        category.setCategoryName(standardized);

        category = categoryRespository.save(category);
        return new CategoryDTO.Response(
            category.getId(),
            category.getCategoryName()
        );
    }

    public void deleteCategory(Long id) {
        Category category = categoryRespository.findById(id).orElse(null);
        if (category == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
        categoryRespository.deleteById(id);
    }
}
