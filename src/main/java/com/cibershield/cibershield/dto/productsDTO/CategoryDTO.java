package com.cibershield.cibershield.dto.productsDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

        @Schema(name = "CategoryCreate")
        public record Create(
                        @NotBlank(message = "El nombre de la categoría es obligatorio") 
                        String categoryName) {
        }
        @Schema(name = "CategoryUpdate")
        public record Update(
                        String categoryName) {
        }
        @Schema(name = "CategoryResponse")      
        public record Response(
                        Long id,
                        String categoryName) {
        }

}
