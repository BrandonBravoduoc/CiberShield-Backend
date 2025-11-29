package com.cibershield.cibershield.dto.userDto;

import lombok.Data;



@Data
public class UserUpdateDTO {
    private Long id;
    private String newUserName;
    private String newEmail;
    private String newPassword; 
    private String confirmPassword;

}
