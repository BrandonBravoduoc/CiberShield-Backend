package com.cibershield.cibershield.dto.orderDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderCreateDTO(
        @NotEmpty(message = "Debe agregar al menos un producto al carrito") @Valid List<OrderCreateItemDTO.Create> items) {
}
