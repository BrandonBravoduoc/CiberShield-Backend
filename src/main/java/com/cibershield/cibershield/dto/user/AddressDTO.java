package com.cibershield.cibershield.dto.user;

public class AddressDTO {

    // Para crear una dirección
    public record Create(
        String street,      // ej: "Avenida Siempre Viva"
        String number,      // ej: "742" o "123 Depto 301"
        Long communeId      // ID de la comuna (obligatorio)
    ) {}

    // Para actualizar una dirección
    public record Update(
        String street,
        String number,
        Long communeId      // puede cambiar de comuna si quieres
    ) {}

    // Lo que recibe el frontend (respuesta completa)
    public record Response(
        Long id,
        String street,
        String number,
        Long communeId,
        String communeName,         // ej: "Santiago"
        Long regionId,
        String regionName           // ej: "Región Metropolitana"
    ) {}

    // Para combos (ej: seleccionar dirección del usuario)
    public record Combo(
        Long id,
        String fullAddress          // ej: "Av. Siempre Viva 742, Santiago, Región Metropolitana"
    ) {}
}
