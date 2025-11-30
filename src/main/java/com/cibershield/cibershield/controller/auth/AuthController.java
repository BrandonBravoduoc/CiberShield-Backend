package com.cibershield.cibershield.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.dto.auth.AuthResponseDTO;
import com.cibershield.cibershield.dto.auth.LoginDTO;
import com.cibershield.cibershield.dto.user.UserDTO;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.service.jwt.JwtService;
import com.cibershield.cibershield.service.user.UserService;
import com.sun.jersey.api.ConflictException;

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



    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO.Register dto) {
        
        try {
            UserDTO.Response response = userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }
    

   @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail().trim().toLowerCase())
        .orElseThrow(() -> new BadCredentialsException("El correo ingresado no existe."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("La contraseña es incorrecta.");
        }
        String token = jwtService.generateToken(user);
        UserDTO.Response userResponse = new UserDTO.Response(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            user.getUserRole().getRoleName()
        );

        AuthResponseDTO response = new AuthResponseDTO(token, userResponse);

        return ResponseEntity.ok(response);
    }
}
