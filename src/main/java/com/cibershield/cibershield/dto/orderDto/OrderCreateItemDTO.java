package com.cibershield.cibershield.dto.orderDto;

import lombok.Data;

@Data
public class OrderCreateItemDTO {
    private Long productId;
    private Integer amount;
    private Long shippingMethodId;
}