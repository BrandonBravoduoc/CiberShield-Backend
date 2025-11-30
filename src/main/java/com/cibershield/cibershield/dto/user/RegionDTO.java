package com.cibershield.cibershield.dto.user;


public class RegionDTO {

    public record Create(
        String regionName
    ) {}


    public record Update(
        String regionName
    ) {}


    public record Response(
        Long id,
        String regionName,
        int communesCount           
    ) {}


    public record Combo(
        Long id,
        String regionName
    ) {}


 
}