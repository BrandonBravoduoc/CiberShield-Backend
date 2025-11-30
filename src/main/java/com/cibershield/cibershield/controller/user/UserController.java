package com.cibershield.cibershield.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.dto.user.ContactDTO;
import com.cibershield.cibershield.dto.user.UserDTO.Profile;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.service.user.ContactService;
import com.cibershield.cibershield.service.user.UserService;

import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;


    @GetMapping
    public ResponseEntity<Profile> myProfile(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.myProfile(currentUser));
    }
    
    @PostMapping("/contact")
    public ResponseEntity<?> createContact(
        @AuthenticationPrincipal User currentUser,
        @Valid @RequestBody ContactDTO.CreateContactWithAddress dto) {
        
        try{
            ContactDTO.Response response = contactService.contactCreateWithAddress(dto, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor.");
        }
    }
    


}
