package com.cibershield.cibershield.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.service.util.JwtService;

import java.util.Objects;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    public boolean isCurrentUserAdmin() {
        Long userId = getCurrentUserId();
        return userRepository.findIdAndRoleNameById(userId)
                .map(roleName -> "ADMIN".equals(roleName))
                .orElse(false);
    }


    public Long getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token no encontrado");
        }

        String token = authHeader.substring(7);

        if (!jwtService.isValid(token)) {
            throw new RuntimeException("Token inválido o expirado");
        }

        return jwtService.getUserIdFromToken(token);
    }
}
