    package com.cibershield.cibershield.service.util;

    import com.auth0.jwt.JWT;
    import com.auth0.jwt.algorithms.Algorithm;
    import com.cibershield.cibershield.model.user.User;

    import org.springframework.stereotype.Service;

    import java.util.Date;

    @Service
    public class JwtService {

        private static final String SECRET = "miClaveSecretaParaLaUni2025";
        private static final long EXPIRATION = 1000 * 60 * 60 * 24; 

        private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

        public String generateToken(User user) {
            return JWT.create()
                    .withSubject(user.getEmail()) 
                    .withClaim("userId", user.getId()) 
                    .withClaim("role", user.getUserRole().getNameRole())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                    .sign(algorithm);
        }

        public String getEmailFromToken(String token) {
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        }

        public Long getUserIdFromToken(String token) {
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("userId")
                    .asLong();
        }

        public boolean isValid(String token) {
            try {
                JWT.require(algorithm).build().verify(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }
