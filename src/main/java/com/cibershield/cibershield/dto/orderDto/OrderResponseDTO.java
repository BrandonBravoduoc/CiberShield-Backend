package com.cibershield.cibershield.dto.orderDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String orderNumber,
        LocalDate orderDate,
        BigDecimal total,
        String status,
        String userName,
        List<OrderDetailResponseDTO> details) {
}
