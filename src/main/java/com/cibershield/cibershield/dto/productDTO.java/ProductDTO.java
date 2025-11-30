
import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductDTO {

    public record Create(
            @NotBlank(message = "El nombre del producto es obligatorio") 
            String productName,
            
            @NotNull(message = "El stock es obligatorio") 
            @Min(value = 0, message = "El stock no puede ser negativo") 
            Integer stock,

            @NotNull(message = "El precio es obligatorio")
            @Positive(message = "El precio debe ser mayor a cero") 
            BigDecimal price,
            String url,

            @NotNull(message = "Debes seleccionar una subcategoría") 
            Long subCategoryId,

            @NotNull(message = "Debes seleccionar una marca") 
            Long tradeMarkId) {
    }

    public record Response(
            Long id,
            String productName,
            Integer stock,
            BigDecimal price,
            String imageUrl,
            String subCategoryName,
            String categoryName,
            String tradeMarkName) {
    }

    public record Update(
            String productName,
            Integer stock,
            BigDecimal price,
            String url,
            Long subCategoryId,
            Long tradeMarkId) {
    }

    public record Combo(
            Long id,
            String productName) {
    }
}