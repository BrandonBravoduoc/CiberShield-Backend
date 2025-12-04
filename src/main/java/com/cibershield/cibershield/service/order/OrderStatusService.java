package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.OrderStatusDTO;
import com.cibershield.cibershield.model.order.OrderStatus;
import com.cibershield.cibershield.repository.order.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public OrderStatus searchById(Long id) {
        return orderStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado de orden no encontrado."));
    }

    public OrderStatus searchByName(String name) {
        return orderStatusRepository.findByName(name.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Estado de orden no encontrado: " + name));
    }

    public List<OrderStatus> searchAll() {
        return orderStatusRepository.findAll();
    }

    public OrderStatus getPendingStatus() {
        return searchByName("PENDIENTE");
    }

    public OrderStatusDTO.Response create(OrderStatusDTO.Create dto) {
        validateStatusDTO(dto);

        if (orderStatusRepository.findByName(dto.name().toUpperCase()).isPresent()) {
            throw new RuntimeException("El estado ya existe.");
        }

        OrderStatus status = new OrderStatus();
        status.setName(dto.name().toUpperCase());
        status.setDescription(dto.description());

        status = orderStatusRepository.save(status);

        return mapToResponseDTO(status);
    }

    public OrderStatusDTO.Response update(Long id, OrderStatusDTO.Create dto) {
        validateStatusDTO(dto);

        OrderStatus status = searchById(id);

        if (orderStatusRepository.findByName(dto.name().toUpperCase()).isPresent()
                && !status.getName().equals(dto.name().toUpperCase())) {
            throw new RuntimeException("El estado ya existe.");
        }

        status.setName(dto.name().toUpperCase());
        status.setDescription(dto.description());

        status = orderStatusRepository.save(status);

        return mapToResponseDTO(status);
    }

    public void delete(Long id) {
        if (!orderStatusRepository.existsById(id)) {
            throw new RuntimeException("Estado de orden no encontrado.");
        }
        orderStatusRepository.deleteById(id);
    }

    private void validateStatusDTO(OrderStatusDTO.Create dto) {
        if (dto == null || dto.name() == null || dto.name().trim().isBlank()) {
            throw new RuntimeException("El nombre del estado es obligatorio.");
        }
    }

    private OrderStatusDTO.Response mapToResponseDTO(OrderStatus status) {
        return new OrderStatusDTO.Response(
                status.getId(),
                status.getName(),
                status.getDescription());
    }
}