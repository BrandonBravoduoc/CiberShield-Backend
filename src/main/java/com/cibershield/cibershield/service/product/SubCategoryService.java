package com.cibershield.cibershield.service.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.cibershield.cibershield.dto.productsDTO.SubCategoryDTO;
import com.cibershield.cibershield.model.product.Category;
import com.cibershield.cibershield.model.product.SubCategory;
import com.cibershield.cibershield.repository.product.CategoryRespository;
import com.cibershield.cibershield.repository.product.SubCategoryRepository;

public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRespository categoryRepository;

    public List<SubCategoryDTO.Response> findAll() {
        return subCategoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SubCategoryDTO.Response findById(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada."));
        return mapToResponse(subCategory);
    }

    public SubCategoryDTO.Response create(SubCategoryDTO.Create dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("La categoría padre no existe."));
        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryName(dto.subCategoryName());
        subCategory.setCategory(category); 
        subCategory = subCategoryRepository.save(subCategory);
        return mapToResponse(subCategory);
    }

    public void delete(Long id) {
        if (!subCategoryRepository.existsById(id)) {
            throw new RuntimeException("Subcategoría no encontrada.");
        }
        subCategoryRepository.deleteById(id);
    }

    private SubCategoryDTO.Response mapToResponse(SubCategory s) {
        return new SubCategoryDTO.Response(
                s.getId(),
                s.getSubCategoryName(),
                s.getCategory() != null ? s.getCategory().getCategoryName() : "Sin Categoría");
    }

}
