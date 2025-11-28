package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.OrderCreateDTO;
import com.cibershield.cibershield.dto.orderDto.OrderCreateItemDTO;
import com.cibershield.cibershield.dto.orderDto.OrderDetailResponseDTO;
import com.cibershield.cibershield.dto.orderDto.OrderResponseDTO;
import com.cibershield.cibershield.model.order.Order;
import com.cibershield.cibershield.model.order.OrderDetail;
import com.cibershield.cibershield.model.order.ShippingMethod;
import com.cibershield.cibershield.model.product.Product;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.order.OrderRepository;
import com.cibershield.cibershield.repository.product.ProductRepository;
import com.cibershield.cibershield.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public OrderResponseDTO createOrder(OrderCreateDTO dto, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Debes iniciar sesión para realizar un pedido");
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new RuntimeException("Tu carrito está vacío");
        }

        User client = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderNumber("ORD-" + String.format("%06d", orderRepository.count() + 1));
        order.setUser(client);
        order.setStatus(orderStatusService.getPendingStatus());

        List<OrderDetail> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderCreateItemDTO item : dto.getItems()) {

            if (item.getAmount() == null || item.getAmount() < 1) {
                throw new RuntimeException("Cantidad inválida");
            }
            if (item.getShippingMethodId() == null) {
                throw new RuntimeException("Debes elegir un método de envío");
            }

            Product producto = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no disponible"));

            ShippingMethod shipping = shippingMethodService.findById(item.getShippingMethodId());

            OrderDetail detalle = orderDetailService.createOrderDetail(order, producto, item, shipping);

            detalles.add(detalle);
            total = total.add(detalle.getSubtotal()).add(shipping.getShippingCost());
        }

        order.setDetails(detalles);
        order.setTotal(total);

        Order guardada = orderRepository.save(order);

        return toResponseDTO(guardada, client);
    }

    private OrderResponseDTO toResponseDTO(Order o, User u) {
        OrderResponseDTO r = new OrderResponseDTO();
        r.setId(o.getId());
        r.setOrderNumber(o.getOrderNumber());
        r.setOrderDate(o.getOrderDate());
        r.setTotal(o.getTotal());
        r.setStatus(o.getStatus().getName());
        r.setUserName(u.getUserName());

        List<OrderDetailResponseDTO> lista = new ArrayList<>();
        for (OrderDetail d : o.getDetails()) {
            OrderDetailResponseDTO odr = new OrderDetailResponseDTO();
            odr.setProductId(d.getProduct().getId());
            odr.setProductName(d.getProduct().getProductName());
            odr.setQuantity(d.getAmount());
            odr.setUnitPrice(d.getUnitPrice());
            odr.setSubtotal(d.getSubtotal());
            lista.add(odr);
        }
        r.setDetails(lista);
        return r;
    }
}