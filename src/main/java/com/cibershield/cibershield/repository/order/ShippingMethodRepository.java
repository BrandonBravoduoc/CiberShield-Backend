package com.cibershield.cibershield.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.order.ShippingMethod;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Integer> {

    boolean existsByMethodName(String methodName);
}
