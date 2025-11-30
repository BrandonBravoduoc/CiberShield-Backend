package com.cibershield.cibershield.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cibershield.cibershield.model.user.User;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "miClaveSecretaParaLaUni2025";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 horas

    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    // GENERAR TOKEN
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail()) // correo va como subject
                .withClaim("userId", user.getId()) // claim de id
                .withClaim("role", user.getUserRole().getNameRole()) // claim del rol
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(algorithm);
    }

    // OBTENER EMAIL (subject)
    public String getEmailFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    // OBTENER ID DEL USUARIO
    public Long getUserIdFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getClaim("userId")
                .asLong();
    }

    // VALIDAR TOKEN
    public boolean isValid(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
