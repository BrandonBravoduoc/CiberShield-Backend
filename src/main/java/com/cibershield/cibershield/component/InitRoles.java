package com.cibershield.cibershield.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cibershield.cibershield.model.user.UserRole;
import com.cibershield.cibershield.repository.user.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitRoles implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(String... args) {

        String defaultRole = "CLIENTE";

        if (!userRoleRepository.existsByNameRole(defaultRole)) {
            
            UserRole newRole = new UserRole();
            newRole.setNameRole(defaultRole);
            
            userRoleRepository.save(newRole);

            System.out.println("Rol 'CLIENTE' creado automáticamente.");
        } else {
            System.out.println("ℹEl rol 'CLIENTE' ya existe. No se creó nuevamente.");
        }
    }
}
