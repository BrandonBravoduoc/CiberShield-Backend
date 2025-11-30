package com.cibershield.cibershield.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.user.UserRole;


@Repository
public interface UserRoleRepository extends JpaRepository <UserRole, Long>{

   Optional<UserRole> findByNameRole(String nameRole);

    boolean existsByNameRole(String nameRole);

    int countByUserRoleNameRole(String nameRole);

    
}
