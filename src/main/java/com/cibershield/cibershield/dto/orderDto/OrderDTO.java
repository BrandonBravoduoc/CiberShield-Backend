package com.cibershield.cibershield.dto.orderDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrderDTO {

       public record OrderCreateDTO(
                Long paymentMethodId,
                Long shippingMethodId,
                BigDecimal total,
                Map<String, String> cardInfo,
                List<OrderDTO.CreateItem> items
        ) {}


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
                OrderStatusDTO.Response status, 
                List<OrderDTO.OrderDetailResponse> details
        ) {}

}
