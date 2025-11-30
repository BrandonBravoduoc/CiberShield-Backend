package com.cibershield.cibershield.dto.user;

public class RoleDTO {
    
    public record CreateRole(
        String nameRole
    ){}

    
    public record Response(
        Long id,
        String nameRole
    ){}

}
