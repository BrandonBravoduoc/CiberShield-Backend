package com.cibershield.cibershield.dto.orderDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDTO {

        public record Create(
                        BigDecimal amount,
                        LocalDate paymentDate,
                        String paymentStatus,
                        Long paymentMethodId,
                        Long orderId) {
        }

        public record Update(
                        BigDecimal amount,
                        LocalDate paymentDate,
                        String paymentStatus,
                        Long paymentMethodId) {
        }

        public record Response(
                        Long id,
                        BigDecimal amount,
                        LocalDate paymentDate,
                        String paymentStatus,
                        Long paymentMethodId,
                        String paymentMethodName,
                        Long orderId) {
        }
}
