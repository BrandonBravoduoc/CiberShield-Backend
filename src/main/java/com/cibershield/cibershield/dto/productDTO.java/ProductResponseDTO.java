import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private Integer Stock;
    private BigDecimal price;
    private String url;
    private String subCategoryName;
    private String tradeMarkName;
}