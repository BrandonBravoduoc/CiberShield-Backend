package com.cibershield.cibershield.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.user.User;


@Repository
public interface UserRepository extends JpaRepository <User, Long>{

    Optional<User>findByUserName(String userName);

    Optional<User>findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserRole(Long roleId);

    int countByUserRole_NameRole(String nameRole);

    @Query("SELECT u.userRole.nameRole FROM User u WHERE u.id = :id AND u.asset = true")
    Optional<String> findIdAndRoleNameById(@Param("id") Long id);
}
