package com.cibershield.cibershield.dto.auth;


import com.cibershield.cibershield.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private UserDTO.Response user;
}

