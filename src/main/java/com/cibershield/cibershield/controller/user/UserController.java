package com.cibershield.cibershield.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cibershield.cibershield.dto.user.ContactDTO;
import com.cibershield.cibershield.dto.user.UserDTO;
import com.cibershield.cibershield.dto.user.UserDTO.Profile;
import com.cibershield.cibershield.dto.user.UserDTO.UpdateUser;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.service.user.ContactService;
import com.cibershield.cibershield.service.user.UserService;
import com.cibershield.cibershield.util.JwtUtil;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/profile")
    public ResponseEntity<?> myProfile() {
        try {
            Long userId = jwtUtil.getCurrentUserId();

            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Profile profile = userService.myProfile(currentUser);

            return ResponseEntity.ok(profile);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/contact")
    public ResponseEntity<?> createContact(
            @Valid @RequestBody ContactDTO.CreateContactWithAddress dto) {

        try {
            Long userId = jwtUtil.getCurrentUserId();

            ContactDTO.Response response = contactService.contactCreateWithAddress(dto, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PatchMapping(value = "/me", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateCurrentUser(
            @RequestPart(required = false) MultipartFile imageUser,
            @RequestPart(required = false) UserDTO.UpdateUser dto
    ) {
        try {
            Long userId = jwtUtil.getCurrentUserId();

            UserDTO.Response updated = userService.userUpdate(dto, imageUser, userId);

            return ResponseEntity.ok(updated);

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }


    @PatchMapping("/me/change-password")
    public ResponseEntity<?> changeMyPassword(@Valid @RequestBody UserDTO.ChangePassword dto) {
        try {
            Long userId = jwtUtil.getCurrentUserId();
            userService.changeMyPassword(dto, userId);
            return ResponseEntity.ok(
                Map.of("message", "Contraseña actualizada exitosamente.")
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error interno del servidor",
                        "detalle", ex.getMessage()
                    ));
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateContact(@RequestBody ContactDTO.UpdateContactWithAddress dto) {
        try {
            Long userId = jwtUtil.getCurrentUserId();

            ContactDTO.Response response = contactService.updateContactWithAddress(dto, userId);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                        "error", e.getMessage()
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error interno del servidor",
                        "detalle", e.getMessage()
                    ));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
        try{
            userService.delete(id);
            return ResponseEntity.ok(200);
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
