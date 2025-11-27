package com.cibershield.cibershield.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibershield.cibershield.model.user.Address;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

    

    
}
