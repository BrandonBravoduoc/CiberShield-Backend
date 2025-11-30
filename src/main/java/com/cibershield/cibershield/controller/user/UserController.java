package com.cibershield.cibershield.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.dto.user.UserDTO;
import com.cibershield.cibershield.dto.user.UserDTO.Profile;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public ResponseEntity<Profile> myProfile(@AuthenticationPrincipal User currentUser) {
        UserDTO.Profile profile = userService.myProfile(currentUser);
        return ResponseEntity.ok(profile);
    }
    



}
