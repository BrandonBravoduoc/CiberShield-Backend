package com.cibershield.cibershield.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String userName;
    private String email;
    private String roleName;
}

