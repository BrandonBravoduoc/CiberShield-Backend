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
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.countByUserRoleRoleName("ADMIN") == 0) {
            crearAdminInicial();
        }
    }

    private void crearAdminInicial() {
        UserRole roleAdmin = userRoleRepository.findByRoleName("ADMIN")
            .orElseThrow(()-> new RuntimeException("Rol ADMIN no encontrado"));

        User admin = new User();
        admin.setUserName("admin");
        admin.setEmail("admin@cibershield.com");
        admin.setPassword(passwordEncoder.encode("Admin1234!")); 
        admin.setImageUser("https://res.cloudinary.com/dyf3i5iqa/image/upload/cibershield/admin-avatar.png");
        admin.setUserRole(roleAdmin);

        userRepository.save(admin);

        System.out.println("==================================================");
        System.out.println("ADMIN CREADO AUTOMÁTICAMENTE");
        System.out.println("Usuario: admin");
        System.out.println("Contraseña: Admin1234!");
        System.out.println("Cambia la contraseña después del primer login");
        System.out.println("==================================================");
    }
}