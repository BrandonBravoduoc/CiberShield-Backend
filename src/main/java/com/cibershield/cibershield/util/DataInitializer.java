package com.cibershield.cibershield.util;

import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.model.user.UserRole;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.repository.user.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearRolSiNoExiste("ADMIN");
        crearRolSiNoExiste("CLIENTE");

        crearAdminSiNoExiste();
    }

    private void crearRolSiNoExiste(String roleName) {
        if (!userRoleRepository.existsByRoleName(roleName)) {
            UserRole role = new UserRole();
            role.setNameRole(roleName);           // ← CORRECTO: coincide con tu entidad
            userRoleRepository.save(role);
            System.out.println("Rol creado automáticamente: " + roleName);
        }
    }

    private void crearAdminSiNoExiste() {
        if (userRepository.countByUserRoleRoleName("ADMIN") == 0) {
            UserRole roleAdmin = userRoleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado después de crearlo"));

            User admin = new User();
            admin.setUserName("admin");
            admin.setEmail("admin@cibershield.com");
            admin.setPassword(passwordEncoder.encode("Admin1234!"));
            admin.setImageUser("https://res.cloudinary.com/dyf3i5iqa/image/upload/cibershield/default-avatar.png");
            admin.setUserRole(roleAdmin);

            userRepository.save(admin);

            System.out.println("==================================================");
            System.out.println("ADMIN CREADO AUTOMÁTICAMENTE");
            System.out.println("Usuario: admin");
            System.out.println("Contraseña: Admin1234!");
            System.out.println("¡Cambia esta contraseña después del primer login!");
            System.out.println("==================================================");
        }
    }
}