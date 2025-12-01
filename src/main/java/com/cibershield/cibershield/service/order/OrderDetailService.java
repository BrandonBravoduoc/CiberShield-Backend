package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.OrderCreateItemDTO;
import com.cibershield.cibershield.model.order.Order;
import com.cibershield.cibershield.model.order.OrderDetail;
import com.cibershield.cibershield.model.order.ShippingMethod;
import com.cibershield.cibershield.model.product.Product;
import com.cibershield.cibershield.repository.order.OrderDetailRepository;
import com.cibershield.cibershield.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<OrderDetail> searchByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    public OrderDetail searchById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de orden no encontrado."));
    }

    public OrderDetail createOrderDetail(Order order, Product product,
            OrderCreateItemDTO.Create item, ShippingMethod shipping) {

        validateOrderDetail(item, product, shipping);

        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setAmount(item.amount());
        detail.setUnitPrice(product.getPrice());
        detail.setSubtotal(product.getPrice().multiply(new BigDecimal(item.amount())));
        detail.setShippingMethod(shipping);

        reduceProductStock(product, item.amount());

        return orderDetailRepository.save(detail);
    }

    private void validateOrderDetail(OrderCreateItemDTO.Create item, Product product, ShippingMethod shipping) {
        if (item == null || product == null || shipping == null) {
            throw new RuntimeException("Datos del detalle de orden incompletos.");
        }

        if (product.getStock() < item.amount()) {
            throw new RuntimeException("No hay suficiente stock de " + product.getProductName());
        }
    }

    private void reduceProductStock(Product product, Integer amount) {
        int newStock = product.getStock() - amount;
        product.setStock(newStock);
        productRepository.save(product);
    }
}
