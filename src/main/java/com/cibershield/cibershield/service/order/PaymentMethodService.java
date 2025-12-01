package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.PaymentMethodDTO;
import com.cibershield.cibershield.model.order.PaymentMethod;
import com.cibershield.cibershield.repository.order.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethod searchById(Long id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metodo de pago no encontrado."));
    }

    public List<PaymentMethod> searchAll() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethodDTO.Response create(PaymentMethodDTO.Create dto) {
        validatePaymentMethodDTO(dto);

        if (paymentMethodRepository.findByPaymentName(dto.paymentName()).isPresent()) {
            throw new RuntimeException("El metodo de pago ya existe.");
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentName(dto.paymentName().toUpperCase());

        paymentMethod = paymentMethodRepository.save(paymentMethod);

        return mapToResponseDTO(paymentMethod);
    }

    public PaymentMethodDTO.Response update(Long id, PaymentMethodDTO.Create dto) {
        validatePaymentMethodDTO(dto);

        PaymentMethod paymentMethod = searchById(id);

        if (paymentMethodRepository.findByPaymentName(dto.paymentName()).isPresent()
                && !paymentMethod.getPaymentName().equals(dto.paymentName().toUpperCase())) {
            throw new RuntimeException("El metodo de pago ya existe.");
        }

        paymentMethod.setPaymentName(dto.paymentName().toUpperCase());

        paymentMethod = paymentMethodRepository.save(paymentMethod);

        return mapToResponseDTO(paymentMethod);
    }

    public void delete(Long id) {
        if (!paymentMethodRepository.existsById(id)) {
            throw new RuntimeException("Metodo de pago no encontrado.");
        }
        paymentMethodRepository.deleteById(id);
    }

    private void validatePaymentMethodDTO(PaymentMethodDTO.Create dto) {
        if (dto == null || dto.paymentName() == null || dto.paymentName().trim().isBlank()) {
            throw new RuntimeException("El nombre del metodo de pago es obligatorio.");
        }
    }

    private PaymentMethodDTO.Response mapToResponseDTO(PaymentMethod paymentMethod) {
        return new PaymentMethodDTO.Response(
                paymentMethod.getId(),
                paymentMethod.getPaymentName());
    }
}
