package com.cibershield.cibershield.dto.user;

import org.springframework.web.multipart.MultipartFile;



public class UserDTO {
    
    public record Register(
        String userName,
        String email,
        String password,
        String confirmPassword,
        String imageUser
    ) {}

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
        String nameRole
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
    String imageUser,
    ContactDTO.Response contact
    ){}



}
