package com.cibershield.cibershield.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cibershield.cibershield.util.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()     // login, register…
                .requestMatchers("/doc/**").permitAll()          // Swagger y OpenAPI 100 % público
                .requestMatchers("/swagger-ui/**").permitAll()   // por si usas la ruta vieja
                .requestMatchers("/v3/api-docs/**").permitAll()  // por si alguien entra directo
                .anyRequest().authenticated()                    // el resto sigue protegido con JWT
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}