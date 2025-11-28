package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.OrderCreateItemDTO;
import com.cibershield.cibershield.model.order.Order;
import com.cibershield.cibershield.model.order.OrderDetail;
import com.cibershield.cibershield.model.order.ShippingMethod;
import com.cibershield.cibershield.model.product.Product;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class OrderDetailService {

    public OrderDetail createOrderDetail(Order order, Product product,
            OrderCreateItemDTO item, ShippingMethod shipping) {

        if (product.getStock() < item.getAmount()) {
            throw new RuntimeException("No hay suficiente stock de " + product.getProductName());
        }

        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setAmount(item.getAmount());
        detail.setUnitPrice(product.getPrice());
        detail.setSubtotal(product.getPrice().multiply(new BigDecimal(item.getAmount())));
        detail.setShippingMethod(shipping);

        product.setStock(product.getStock() - item.getAmount());

        return detail;
    }
}