package com.cibershield.cibershield.dto.user;

public class RoleDTO {
    
    public record CreateRole(
        String roleName
    ){}

    
    public record Response(
        Long id,
        String roleName
    ){}

}
