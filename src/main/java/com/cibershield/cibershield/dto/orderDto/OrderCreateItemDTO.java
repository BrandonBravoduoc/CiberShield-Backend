package com.cibershield.cibershield.dto.orderDto;

public class OrderCreateItemDTO {

    public record Create(
            Long productId,
            Integer amount,
            Integer shippingMethodId) {
    }
}
