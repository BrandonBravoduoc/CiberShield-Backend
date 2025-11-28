package com.cibershield.cibershield.dto.orderDto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private String orderNumber;
    private LocalDate orderDate;
    private BigDecimal total;
    private String status;
    private String userName;
    private List<OrderDetailResponseDTO> details;
}