
import jakarta.validation.constraints.NotBlank;

public class TradeMarkDTO {

    public record Create(
        @NotBlank(message = "El nombre de la marca es obligatorio")
        String tradeMarkName
    ) {}

    public record Response(
        Long id,
        String tradeMarkName
    ) {}

    public record Combo(
        Long id,
        String tradeMarkName
    ) {}
}