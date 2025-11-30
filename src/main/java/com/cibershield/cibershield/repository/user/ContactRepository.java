package com.cibershield.cibershield.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.user.Contact;


@Repository
public interface ContactRepository extends JpaRepository <Contact , Long>{

    boolean existByPhone(String phone);

    

    
}
