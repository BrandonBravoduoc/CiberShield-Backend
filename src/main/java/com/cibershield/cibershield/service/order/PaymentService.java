package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.PaymentDTO;
import com.cibershield.cibershield.model.order.Order;
import com.cibershield.cibershield.model.order.Payment;
import com.cibershield.cibershield.model.order.PaymentMethod;
import com.cibershield.cibershield.repository.order.OrderRepository;
import com.cibershield.cibershield.repository.order.PaymentMethodRepository;
import com.cibershield.cibershield.repository.order.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public Payment searchById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado."));
    }

    public List<PaymentDTO.Response> searchByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO.Response> searchAll() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO.Response create(PaymentDTO.Create dto) {
        validatePaymentDTO(dto);

        Order order = getOrderOrThrow(dto.orderId());
        PaymentMethod paymentMethod = getPaymentMethodOrThrow(dto.paymentMethodId());

        Payment payment = new Payment();
        payment.setAmount(dto.amount());
        payment.setPaymentDate(dto.paymentDate());
        payment.setPaymentMethod(paymentMethod);
        payment.setOrder(order);

        payment = paymentRepository.save(payment);

        return mapToResponseDTO(payment);
    }

    public PaymentDTO.Response update(Long id, PaymentDTO.Update dto) {
        Payment payment = searchById(id);

        if (dto.amount() != null) {
            if (dto.amount().signum() <= 0) {
                throw new RuntimeException("El monto debe ser positivo."); // XD UNO NUNKA SAE
            }
            payment.setAmount(dto.amount());
        }

        if (dto.paymentDate() != null) {
            payment.setPaymentDate(dto.paymentDate());
        }

        if (dto.paymentMethodId() != null) {
            PaymentMethod paymentMethod = getPaymentMethodOrThrow(dto.paymentMethodId());
            payment.setPaymentMethod(paymentMethod);
        }

        payment = paymentRepository.save(payment);

        return mapToResponseDTO(payment);
    }

    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Pago no encontrado.");
        }
        paymentRepository.deleteById(id);
    }

    private void validatePaymentDTO(PaymentDTO.Create dto) {
        if (dto == null) {
            throw new RuntimeException("Los datos de pago no pueden estar vacios.");
        }
        if (dto.amount() == null || dto.amount().signum() <= 0) {
            throw new RuntimeException("El monto debe ser positivo.");
        }
        if (dto.paymentDate() == null) {
            throw new RuntimeException("La fecha de pago es obligatoria.");
        }
        if (dto.paymentMethodId() == null) {
            throw new RuntimeException("El metodo de pago es obligatorio.");
        }
        if (dto.orderId() == null) {
            throw new RuntimeException("La orden es obligatoria.");
        }
    }

    private Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada."));
    }

    private PaymentMethod getPaymentMethodOrThrow(Long paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Metodo de pago no encontrado."));
    }

    private PaymentDTO.Response mapToResponseDTO(Payment payment) {
        return new PaymentDTO.Response(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getPaymentMethod().getId(),
                payment.getPaymentMethod().getPaymentName(),
                payment.getOrder().getId());
    }
}
