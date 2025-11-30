package com.cibershield.cibershield.dto.user;

public class AddressDTO {

    public record Create(
        String street,     
        String number,     
        Long communeId     
    ) {}


    public record Update(
        String street,
        String number,
        Long communeId      
    ) {}

   
    public record Response(
        Long id,
        String street,
        String number,
        Long communeId,
        String communeName,         
        Long regionId,
        String regionName          
    ) {}

 
    public record Combo(
        Long id,
        String fullAddress         
    ) {}
}
