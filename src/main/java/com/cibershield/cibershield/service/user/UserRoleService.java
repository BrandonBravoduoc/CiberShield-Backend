package com.cibershield.cibershield.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.user.RoleDTO;
import com.cibershield.cibershield.model.user.UserRole;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.repository.user.UserRoleRepository;

@Service
public class UserRoleService {


    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;
 
    
    public RoleDTO.Response createRole(RoleDTO.CreateRole dto){

        String roleName = dto.roleName().trim().toUpperCase();
        if(roleName == null || dto.roleName().isEmpty()){
            throw new RuntimeException("El nombre del rol es obligatorio.");
        }
        if(userRoleRepository.existsByRoleName(roleName)){
            throw new RuntimeException("El rol ya existe");
        }
        UserRole role = new UserRole();
        role.setRoleName(roleName);

        role = userRoleRepository.save(role);

        return new RoleDTO.Response(role.getId(), role.getRoleName());
    }

    public void deleteRole(Long roleId){

        UserRole role = userRoleRepository.findById(roleId)
            .orElseThrow(()-> new RuntimeException("No se encontró el rol."));
        
        if(userRepository.existsByUserRole(roleId)){
            throw new RuntimeException("No se puede eliminar un rol en uso");
        }
        if("ADMIN".equalsIgnoreCase(role.getRoleName()) || "CLIENTE".equalsIgnoreCase(role.getRoleName())){
            throw new RuntimeException("El rol no se puede eliminar.");
        }
        userRoleRepository.delete(role);
    }



}
