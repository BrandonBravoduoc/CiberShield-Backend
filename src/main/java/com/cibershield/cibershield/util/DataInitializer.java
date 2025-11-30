package com.cibershield.cibershield.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.model.user.UserRole;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.repository.user.UserRoleRepository;

import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearRolesSiNoExisten();
        crearAdminSiNoExiste();
    }

    private void crearRolesSiNoExisten() {
        crearRolSiNoExiste("ADMIN");
        crearRolSiNoExiste("CLIENTE");
    }

    private void crearRolSiNoExiste(String roleName) {
        if (!userRoleRepository.existsByRoleName(roleName)) {
            UserRole role = new UserRole();
            role.setRoleName(roleName);
            userRoleRepository.save(role);
            System.out.println("Rol creado: " + roleName);
        }
    }

    private void crearAdminSiNoExiste() {
        if (userRepository.countByUserRoleRoleName("ADMIN") == 0) {
            UserRole roleAdmin = userRoleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN debería existir ahora"));

            User admin = new User();
            admin.setUserName("admin");
            admin.setEmail("admin@cibershield.com");
            admin.setPassword(passwordEncoder.encode("Admin1234!"));
            admin.setImageUser("https://res.cloudinary.com/dyf3i5iqa/image/upload/cibershield/default-avatar.png");
            admin.setUserRole(roleAdmin);

            userRepository.save(admin);
            System.out.println("ADMIN CREADO → usuario: admin | contraseña: Admin1234!");
        }
    }
}