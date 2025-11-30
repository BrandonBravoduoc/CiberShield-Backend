package com.cibershield.cibershield.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    

    public record Register(

        @NotBlank(message = "El nombre de usuario es obligatorio.")
        String userName,
        
        @Email(message = "Correo invalido.")
        String email,

        @NotBlank(message = "La contraseña dene tener al menos 8 carácteres.")
        String password,
        String confirmPassword
    ){}


    public record Response(
        Long id,
        String userName,
        String email,
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

}
