package com.cibershield.cibershield.dto.user;

public class ContactDTO {

    public record CreateContact(
        String name,
        String lastName,
        String phone,
        Long addressId
    ){}


    public record Response(
        Long id,
        String name,
        String lastName,
        String phone,
        String addressInfo,
        String userName
     
    ) {}

    
}
