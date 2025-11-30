package com.cibershield.cibershield.controller.user;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.dto.user.CommuneDTO;
import com.cibershield.cibershield.dto.user.RegionDTO;
import com.cibershield.cibershield.dto.user.RoleDTO;
import com.cibershield.cibershield.dto.user.UserDTO;
import com.cibershield.cibershield.service.user.CommuneService;
import com.cibershield.cibershield.service.user.RegionService;
import com.cibershield.cibershield.service.user.UserRoleService;
import com.cibershield.cibershield.service.user.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommuneService communeService;
    
    @Autowired
    private RegionService regionService;

    @Autowired
    private UserRoleService userRoleService;


    


// =================== ENDPOINTS TO CREATE =====================

    @PostMapping("/role")
    public ResponseEntity<?> createRole(
        @RequestBody RoleDTO.CreateRole dto){

        try{
            RoleDTO.Response response = userRoleService.createRole(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error interno del servidor");
        }
    }
    

   @PostMapping("/regions/{regionId}/communes")
    public ResponseEntity<?> createCommune(
            @PathVariable Long regionId,
            @Valid @RequestBody CommuneDTO.Create dto) {

        try {
            CommuneDTO.Response response = communeService.createCommune(dto, regionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }
    

    @PostMapping("/region")
    public ResponseEntity<?> createRegion(@Valid @RequestBody RegionDTO.Create dto) {

        try {
            RegionDTO.Response response = regionService.createRegion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor.");
        }
    }

// =================== ENDPOINTS TO READ =====================

   @GetMapping("/users")
    public ResponseEntity<List<UserDTO.Response>> listUsers() {
        try {
            List<UserDTO.Response> users = userService.listUsers();
            return ResponseEntity.ok(users);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Collections.emptyList());
        }
    }
    
    @GetMapping("/user/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email){
        try{
            UserDTO.Response user = userService.findByEmail(email);
            return ResponseEntity.ok(user);

        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }
    
    


// =================== ENDPOINTS TO UPDATE =====================




// =================== ENDPOINTS TO DELETE =====================


    @DeleteMapping("/communes/{communeId}")
    public ResponseEntity<?> deleteCommune(@PathVariable Long communeId){
        try{
                communeService.deleteCommune(communeId);
                return ResponseEntity.noContent().build();

        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }



}
