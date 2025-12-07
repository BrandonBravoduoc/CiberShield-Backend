package com.cibershield.cibershield.dto.productsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubCategoryDTO {
        @Schema(name = "SubCategoryCreate")
        public record Create(
                        @NotBlank(message = "El nombre es obligatorio") String subCategoryName,

                        @NotNull(message = "Debes indicar la categoría padre") Long categoryId) {
        }

        @Schema(name = "SubCategoryResponse")
        public record Response(
                        Long id,
                        String subCategoryName,
                        String categoryName) {
        }

        @Schema(name = "SubCategoryUpdate")
        public record Update(
                        String subCategoryName,
                        Long categoryId) {
        }

        @Schema(name = "SubCategoryCombo")
        public record Combo(
                        Long id,
                        String subCategoryName) {
        }
}