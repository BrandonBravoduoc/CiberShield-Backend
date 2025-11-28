package com.cibershield.cibershield.dto.orderDto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDetailResponseDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}