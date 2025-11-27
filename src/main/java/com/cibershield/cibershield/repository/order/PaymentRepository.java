package com.cibershield.cibershield.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.order.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
