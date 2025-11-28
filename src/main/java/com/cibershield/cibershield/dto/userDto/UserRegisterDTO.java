package com.cibershield.cibershield.dto.userDto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String userName;
    private String email;
    private String password;
    private String confirmPassword;
}
