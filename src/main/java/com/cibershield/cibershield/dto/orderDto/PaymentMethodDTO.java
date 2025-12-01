package com.cibershield.cibershield.dto.orderDto;

import jakarta.validation.constraints.NotBlank;

public class PaymentMethodDTO {

        public record Create(
                        @NotBlank(message = "El nombre del metodo de pago es obligatorio") String paymentName) {
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
