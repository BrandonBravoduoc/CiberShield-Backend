package com.cibershield.cibershield.dto.orderDto;

public class OrderStatusDTO {

        public record Create(
                        String name,
                        String description) {
        }

        public record Response(
                        Long id,
                        String name,
                        String description) {
        }

        public record Combo(
                        Long id,
                        String name) {
        }
}
