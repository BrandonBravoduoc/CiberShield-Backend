package com.cibershield.cibershield.dto.orderDto;

public class PaymentMethodDTO {

        public record Create(
                        String paymentName) {
        }

        public record Response(
                        Long id,
                        String paymentName) {
        }

        public record Combo(
                        Long id,
                        String paymentName) {
        }
}
