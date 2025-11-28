package com.cibershield.cibershield.dto.orderDto;

import java.util.List;

import lombok.Data;

@Data
public class OrderCreateDTO {
    private List<OrderCreateItemDTO> items;
}
