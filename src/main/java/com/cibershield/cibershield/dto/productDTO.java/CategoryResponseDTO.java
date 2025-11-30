import jakarta.validation.constraints.NotBlank;

public class CategoryResponseDTO {

    public record Create(   
        @NotBlank(message = "El nombre de la categoría es obligatorio")
        String categoryName
    ) {}

    public record Response(
        Long id,
        String categoryName
    ) {}
}
