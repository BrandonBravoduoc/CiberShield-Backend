package com.cibershield.cibershield.dto.orderDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderDTO {

        public record OrderCreate(
                        List<OrderDTO.CreateItem> items) {
        }

        public record CreateItem(
                        Long productId,
                        Integer quantity,
                        Integer shippingMethodId) {
        }

        public record OrderDetailResponse(
                        Long productId,
                        String productName,
                        Integer amount,
                        BigDecimal unitPrice,
                        BigDecimal subtotal) {
        }

        public record OrderResponseDTO(
                        Long id,
                        String orderNumber,
                        LocalDate orderDate,
                        BigDecimal total,
                        String status,
                        String userName,
                        List<OrderDTO.OrderDetailResponse> details) {
        }

}
