package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.model.order.OrderStatus;
import com.cibershield.cibershield.repository.order.OrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public OrderStatus getPendingStatus() {
        return orderStatusRepository.findByName("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado 'PENDIENTE' no ha sido creado"));
    }
}