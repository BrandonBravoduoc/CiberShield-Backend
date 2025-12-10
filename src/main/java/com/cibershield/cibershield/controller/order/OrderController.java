package com.cibershield.cibershield.controller.order;

import com.cibershield.cibershield.dto.orderDto.OrderDTO.OrderCreateDTO;
import com.cibershield.cibershield.dto.orderDto.OrderStatusDTO;
import com.cibershield.cibershield.model.order.Order;
import com.cibershield.cibershield.service.order.OrderService;
import com.cibershield.cibershield.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Order>> list() {
        try {
            return ResponseEntity.ok(orderService.searchAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.searchById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderCreateDTO dto) {
        try {
            Long userId = 1L;
            return new ResponseEntity<>(orderService.create(dto, userId), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody OrderStatusDTO.Create dto) {
        try {
            jwtUtil.checkAdmin();
            return ResponseEntity.ok(orderService.updateOrderStatus(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            jwtUtil.checkAdmin();
            orderService.searchById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
