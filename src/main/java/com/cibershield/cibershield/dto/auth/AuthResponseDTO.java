package com.cibershield.cibershield.dto.auth;

import com.cibershield.cibershield.dto.userDto.UserResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private UserResponseDTO user;
}

