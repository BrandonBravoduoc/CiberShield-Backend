package com.cibershield.cibershield.dto.user;

public class ContactDTO {

    public record CreateContactWithAddress(
        String name,
        String lastName,
        String phone,
        String street,
        String number,
        Long communeId
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
