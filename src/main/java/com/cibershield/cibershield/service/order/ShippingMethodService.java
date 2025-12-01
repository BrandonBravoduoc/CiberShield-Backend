package com.cibershield.cibershield.service.order;

import com.cibershield.cibershield.dto.orderDto.ShippingMethodDTO;
import com.cibershield.cibershield.model.order.ShippingMethod;
import com.cibershield.cibershield.repository.order.ShippingMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShippingMethodService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    public ShippingMethod searchById(Integer id) {
        return shippingMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metodo de envio no encontrado."));
    }

    public List<ShippingMethodDTO.Response> searchAll() {
        return shippingMethodRepository.findAll().stream()
                .filter(ShippingMethod::isActiveStatus)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ShippingMethodDTO.Response> searchAllIncludingInactive() {
        return shippingMethodRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ShippingMethodDTO.Response create(ShippingMethodDTO.Create dto) {
        validateShippingMethodDTO(dto);

        if (shippingMethodRepository.existsByMethodName(dto.methodName())) {
            throw new RuntimeException("El metodo de envio ya existe.");
        }

        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setMethodName(dto.methodName().toUpperCase());
        shippingMethod.setShippingCost(dto.shippingCost());
        shippingMethod.setActiveStatus(dto.activeStatus());

        shippingMethod = shippingMethodRepository.save(shippingMethod);

        return mapToResponseDTO(shippingMethod);
    }

    public ShippingMethodDTO.Response update(Integer id, ShippingMethodDTO.Update dto) {
        validateShippingMethodDTO(new ShippingMethodDTO.Create(
                dto.methodName(),
                dto.shippingCost(),
                dto.activeStatus()));

        ShippingMethod shippingMethod = searchById(id);

        if (shippingMethodRepository.existsByMethodName(dto.methodName())
                && !shippingMethod.getMethodName().equals(dto.methodName().toUpperCase())) {
            throw new RuntimeException("El metodo de envio ya existe.");
        }

        shippingMethod.setMethodName(dto.methodName().toUpperCase());
        shippingMethod.setShippingCost(dto.shippingCost());
        shippingMethod.setActiveStatus(dto.activeStatus());

        shippingMethod = shippingMethodRepository.save(shippingMethod);

        return mapToResponseDTO(shippingMethod);
    }

    public void delete(Integer id) {
        if (!shippingMethodRepository.existsById(id)) {
            throw new RuntimeException("Metodo de envio no encontrado.");
        }
        shippingMethodRepository.deleteById(id);
    }

    private void validateShippingMethodDTO(ShippingMethodDTO.Create dto) {
        if (dto == null || dto.methodName() == null || dto.methodName().trim().isBlank()) {
            throw new RuntimeException("El nombre del metodo de envio es obligatorio.");
        }
        if (dto.shippingCost() == null || dto.shippingCost().signum() <= 0) {
            throw new RuntimeException("El costo de envio debe ser positivo.");
        }
    }

    private ShippingMethodDTO.Response mapToResponseDTO(ShippingMethod shippingMethod) {
        return new ShippingMethodDTO.Response(
                shippingMethod.getId(),
                shippingMethod.getMethodName(),
                shippingMethod.getShippingCost(),
                shippingMethod.isActiveStatus());
    }
}
