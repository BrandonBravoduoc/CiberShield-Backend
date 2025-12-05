package com.cibershield.cibershield.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.model.user.UserRole;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.repository.user.UserRoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ------------------------------
            // Crear Roles por defecto
            // ------------------------------
            UserRole adminRole = roleRepository.findByNameRole("ADMINISTRADOR")
                    .orElseGet(() -> {
                        UserRole role = new UserRole();
                        role.setNameRole("ADMINISTRADOR");
                        return roleRepository.save(role);
                    });

            UserRole clientRole = roleRepository.findByNameRole("CLIENTE")
                    .orElseGet(() -> {
                        UserRole role = new UserRole();
                        role.setNameRole("CLIENTE");
                        return roleRepository.save(role);
                    });

            System.out.println("Roles creados/verificados.");

            // ------------------------------
            // Crear usuario administrador
            // ------------------------------
            String adminEmail = "admin@cibershield.com";

            if (!userRepository.existsByEmail(adminEmail)) {

                User admin = new User();
                admin.setUserName("Administrador");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Admin123$")); // ← contraseña segura
                admin.setUserRole(adminRole);
                admin.setImageUser("https://res.cloudinary.com/dyf3i5iqa/image/upload/cibershield/default-avatar.png");

                userRepository.save(admin);

                System.out.println("Usuario administrador creado.");
            } else {
                System.out.println("El usuario administrador ya existe.");
            }
        };
    }
}
