package com.cibershield.cibershield.util;

import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Rutas públicas → NO filtrar
        return path.startsWith("/api/v1/auth/") ||   // login & register
               path.startsWith("/swagger") ||        // swagger-ui
               path.startsWith("/v3/api-docs") ||    // openapi docs
               path.startsWith("/doc") ||            // tu ruta personalizada
               path.startsWith("/swagger-ui");       // swagger ui assets
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // El token debe venir así: Bearer eyJh...
        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            if (jwtService.isValid(token)) {

                String email = jwtService.getEmailFromToken(token);

                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {

                    var authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + user.getUserRole().getNameRole())
                    );

                    UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
