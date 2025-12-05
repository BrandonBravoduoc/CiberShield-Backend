package com.cibershield.cibershield.dto.productsDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubCategoryDTO {

        public record Create(
                        @NotBlank(message = "El nombre es obligatorio") String subCategoryName,

                        @NotNull(message = "Debes indicar la categoría padre") Long categoryId) {
        }

        public record Response(
                        Long id,
                        String subCategoryName,
                        String categoryName) {
        }

        public record Update(
                        String subCategoryName,
                        Long categoryId 
        ) {
        }

        public record Combo(
                        Long id,
                        String subCategoryName) {
        }
}