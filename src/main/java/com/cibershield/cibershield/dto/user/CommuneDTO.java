package com.cibershield.cibershield.dto.user;

public class CommuneDTO {

    // Para crear una comuna
    public record Create(
        String nameCommunity,   // nombre de la comuna
        Long regionId           // ID de la región a la que pertenece
    ) {}

    // Para actualizar una comuna
    public record Update(
        String nameCommunity,
        Long regionId           // puede cambiar de región si quieres
    ) {}

    // Respuesta completa (lo que recibe el frontend)
    public record Response(
        Long id,
        String nameCommunity,
        Long regionId,
        String regionName       // nombre de la región (útil en listas)
    ) {}

    // Para <select> / combo (ej: al crear dirección)
    public record Combo(
        Long id,
        String nameCommunity,
        String regionName       // ej: "La Florida - Región Metropolitana"
    ) {}
}
