package com.cibershield.cibershield.dto.userDto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private Long id;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
