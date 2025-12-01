package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.OrderCreateDTO;
import com.cibershield.cibershield.dto.orderDto.OrderCreateItemDTO;
import com.cibershield.cibershield.dto.orderDto.OrderDetailResponseDTO;
import com.cibershield.cibershield.dto.orderDto.OrderResponseDTO;
import com.cibershield.cibershield.dto.orderDto.OrderStatusDTO;
import com.cibershield.cibershield.model.order.Order;
import com.cibershield.cibershield.model.order.OrderDetail;
import com.cibershield.cibershield.model.order.OrderStatus;
import com.cibershield.cibershield.model.order.ShippingMethod;
import com.cibershield.cibershield.model.product.Product;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.order.OrderRepository;
import com.cibershield.cibershield.repository.order.OrderStatusRepository;
import com.cibershield.cibershield.repository.product.ProductRepository;
import com.cibershield.cibershield.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private ShippingMethodService shippingMethodService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public Order searchById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada."));
    }

    public List<Order> searchAll() {
        return orderRepository.findAll();
    }

    public OrderResponseDTO create(OrderCreateDTO dto, Long userId) {
        validateCreateOrder(dto);

        User user = getUserOrNaGrrr(userId); // posible propuesta (segun yo) pq vi q ya no estan usando auth
        Order order = createNewOrder(user);

        BigDecimal total = processOrderItems(order, dto.items());

        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);

        return mapToResponseDTO(savedOrder);
    }

    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatusDTO.Create dto) {
        validateStatusDTO(dto);

        Order order = searchById(orderId);
        OrderStatus status = getOrderStatus(dto.name());

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return mapToResponseDTO(updatedOrder);
    }

    private void validateCreateOrder(OrderCreateDTO dto) {
        if (dto == null || dto.items() == null || dto.items().isEmpty()) {
            throw new RuntimeException("Tu carrito está vacio.");
        }
    }

    private void validateStatusDTO(OrderStatusDTO.Create dto) {
        if (dto == null || dto.name() == null || dto.name().trim().isBlank()) {
            throw new RuntimeException("El nombre del estado es obligatorio.");
        }
    }

    private void validateOrderItem(OrderCreateItemDTO.Create item) {
        if (item.amount() == null || item.amount() < 1) {
            throw new RuntimeException("Cantidad inválida");
        }
        if (item.shippingMethodId() == null) {
            throw new RuntimeException("Debes elegir un metodo de envio.");
        }
    }

    private User getUserOrNaGrrr(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }

    private OrderStatus getOrderStatus(String statusName) {
        return orderStatusRepository.findByName(statusName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Estado de orden no encontrado: " + statusName));
    }

    private Order createNewOrder(User user) {
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderNumber("ORD-" + String.format("%06d", orderRepository.count() + 1));
        order.setUser(user);
        order.setStatus(orderStatusService.getPendingStatus());
        order.setDetails(new ArrayList<>());
        return order;
    }

    private BigDecimal processOrderItems(Order order, List<OrderCreateItemDTO.Create> items) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderCreateItemDTO.Create item : items) {
            validateOrderItem(item);

            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new RuntimeException("Producto no disponible."));

            ShippingMethod shipping = shippingMethodService.searchById(item.shippingMethodId());

            OrderDetail detail = orderDetailService.createOrderDetail(order, product, item, shipping);
            order.getDetails().add(detail);

            total = total.add(detail.getSubtotal()).add(shipping.getShippingCost());
        }

        return total;
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderDate(),
                order.getTotal(),
                order.getStatus().getName(),
                order.getUser().getUserName(),
                mapOrderDetails(order.getDetails()));
    }

    private List<OrderDetailResponseDTO> mapOrderDetails(List<OrderDetail> details) {
        return details.stream()
                .map(this::mapOrderDetail)
                .collect(Collectors.toList());
    }

    private OrderDetailResponseDTO mapOrderDetail(OrderDetail detail) {
        return new OrderDetailResponseDTO(
                detail.getProduct().getId(),
                detail.getProduct().getProductName(),
                detail.getAmount(),
                detail.getUnitPrice(),
                detail.getSubtotal());
    }
}
