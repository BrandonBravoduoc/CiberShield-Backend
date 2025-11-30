package com.cibershield.cibershield.service.product;

import java.util.List;

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

    public List<Category> searchAll() {
        return categoryRepository.findAll();
    }
    public Category searchById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public Category searchByName(String name) {
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public CategoryDTO.Response saveCategory(CategoryDTO.Create dto) {
        if (dto == null) {
            throw new RuntimeException("La categoría no puede estar nula");
        }
        if (dto.categoryName() == null || dto.categoryName().trim().isBlank()) {
            throw new RuntimeException("El nombre de la categoría no puede estar vacío.");
        }

        String standardized = dto.categoryName().trim().toUpperCase();
        if (categoryRepository.existsByCategoryName(standardized)) {
            throw new RuntimeException("El nombre de la categoría ya existe");
        }

        Category category = new Category();
        category.setCategoryName(standardized);

        category = categoryRepository.save(category);
        return new CategoryDTO.Response(
                category.getId(),
                category.getCategoryName());
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada.");
        }
        categoryRepository.deleteById(id);
    }
}