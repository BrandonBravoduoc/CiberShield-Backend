package com.cibershield.cibershield.dto.auth;

import com.cibershield.cibershield.dto.user.UserDTO;

public class AuthDTO {
    

    public record LoginDTO(
        String email,
        String password
    ) {}


    public record authResponse(
        String token,
        UserDTO.Response user
    ){}

}
