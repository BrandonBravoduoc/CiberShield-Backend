package com.cibershield.cibershield.dto.user;

public class CommuneDTO {

   
    public record CreateCommune(
        String nameCommunity,  
        Long regionId           
    ) {}


    public record Update(
        String nameCommunity,
        Long regionId           
    ) {}

 
    public record Response(
        Long id,
        String nameCommunity,
        Long regionId,
        String regionName       
    ) {}

   
    public record Combo(
        Long id,
        String nameCommunity,
        String regionName      
    ) {}
}
