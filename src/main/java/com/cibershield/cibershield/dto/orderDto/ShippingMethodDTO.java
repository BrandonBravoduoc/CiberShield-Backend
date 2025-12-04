package com.cibershield.cibershield.dto.orderDto;

import java.math.BigDecimal;

public class ShippingMethodDTO {
        public record Create(
                        String methodName,
                        BigDecimal shippingCost,
                        boolean activeStatus) {
        }

        public record Update(
                        String methodName,
                        BigDecimal shippingCost,
                        boolean activeStatus) {
        }

        public record Response(
                        Integer id,
                        String methodName,
                        BigDecimal shippingCost,
                        boolean activeStatus) {
        }

        public record Combo(
                        Integer id,
                        String methodName,
                        BigDecimal shippingCost) {
        }
}