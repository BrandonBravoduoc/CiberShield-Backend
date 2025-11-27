package com.cibershield.cibershield.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.user.UserRole;


@Repository
public interface UserRoleRepository extends JpaRepository <UserRole, Long>{

    UserRole findByRoleName(String string);

    

    
}
