package com.cibershield.cibershield.dto.user;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    

    public record Register(

        @NotBlank(message = "El nombre de usuario es obligatorio.")
        String userName,
        String imageUser,
        @Email(message = "Correo invalido.")
        String email,

        @NotBlank(message = "La contraseña dene tener al menos 8 carácteres.")
        String password,
        String confirmPassword
    ){}

    public record RegisterRequest(
        String userName,
        String email,
        String password,
        String confirmPassword,
        MultipartFile imageUser
    ) {}

    public record Response(
        Long id,
        String userName,
        String email,
        String imageUser,
        String roleName
    ){}


    public record UpdateUser(
        String newUserName,
        String newEmail
    ){}


    public record ChangePassword(
        String currentPassword,
        String newPassword,
        String confirmPassword
    ){} 


    public record Profile(
        String userName,
        String email,
        String name,
        String lastName,
        String Phone,
        String adressInfo
    ){}

}
