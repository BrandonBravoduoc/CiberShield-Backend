package com.cibershield.cibershield.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.user.Contact;
import com.cibershield.cibershield.model.user.User;


@Repository
public interface ContactRepository extends JpaRepository <Contact , Long>{

    boolean existByPhone(String phone);

    Optional<Contact> findByUser(User currentUser);

    

    
}
