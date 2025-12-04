package com.cibershield.cibershield.controller.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.dto.auth.AuthResponseDTO;
import com.cibershield.cibershield.dto.auth.LoginDTO;
import com.cibershield.cibershield.dto.user.UserDTO;
import com.cibershield.cibershield.dto.user.UserDTO.RegisterRequest;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.service.user.UserService;
import com.cibershield.cibershield.service.util.CloudinaryService;
import com.cibershield.cibershield.service.util.JwtService;


import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CloudinaryService cloudinaryService;



    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@ModelAttribute RegisterRequest dto) {
        try {

            String imageUrl = "https://res.cloudinary.com/dyf3i5iqa/image/upload/cibershield/default-avatar.png";

            if (dto.imageUser() != null && !dto.imageUser().isEmpty()) {
                imageUrl = cloudinaryService.uploadUserImage(dto.imageUser());
            }

            UserDTO.Register serviceDto = new UserDTO.Register(
                dto.userName(),
                imageUrl,                     
                dto.email(),
                dto.password(),
                dto.confirmPassword()
            );

            UserDTO.Response response = userService.createUser(serviceDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }



    

    @PostMapping("/singin")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new RuntimeException("Debe ingresar un correo.");
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("Debe ingresar una contraseña.");
        }

        User user = userRepository.findByEmail(dto.getEmail().trim().toLowerCase())
            .orElseThrow(() -> new RuntimeException("El correo ingresado no existe."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña es incorrecta.");
        }

        String token = jwtService.generateToken(user);

        UserDTO.Response userResponse = new UserDTO.Response(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            user.getImageUser(),
            user.getUserRole().getNameRole()
        );

        AuthResponseDTO response = new AuthResponseDTO(token, userResponse);

        return ResponseEntity.ok(response);
    }
}
