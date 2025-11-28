package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.model.order.ShippingMethod;
import com.cibershield.cibershield.repository.order.ShippingMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShippingMethodService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    public ShippingMethod findById(Long id) {
        return shippingMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metodo de envio no valido"));
    }
}