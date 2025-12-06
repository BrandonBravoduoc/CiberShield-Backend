package com.cibershield.cibershield.dto.orderDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDTO {

        public record Create(
                        BigDecimal amount,
                        LocalDate paymentDate,
                        Long paymentMethodId,
                        Long orderId) {
        }

        public record Update(
                        BigDecimal amount,
                        LocalDate paymentDate,
                        Long paymentMethodId) {
        }

        public record Response(
                        Long id,
                        BigDecimal amount,
                        LocalDate paymentDate,
                        Long paymentMethodId,
                        String paymentMethodName,
                        Long orderId) {
        }
}
