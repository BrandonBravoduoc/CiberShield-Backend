package com.cibershield.cibershield.dto.orderDto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderListResponseDTO {
    private Long id;
    private String orderNumber;
    private LocalDate orderDate;
    private BigDecimal total;
    private String status;
    private Integer totalItems;
}