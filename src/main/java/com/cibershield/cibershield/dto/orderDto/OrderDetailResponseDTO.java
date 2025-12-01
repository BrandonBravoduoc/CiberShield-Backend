package com.cibershield.cibershield.dto.orderDto;

import java.math.BigDecimal;

public record OrderDetailResponseDTO(
        Long productId,
        String productName,
        Integer amount,
        BigDecimal unitPrice,
        BigDecimal subtotal) {
}
