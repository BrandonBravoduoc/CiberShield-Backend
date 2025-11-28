package com.cibershield.cibershield.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.dto.auth.AuthResponseDTO;
import com.cibershield.cibershield.dto.userDto.LoginDTO;
import com.cibershield.cibershield.dto.userDto.UserRegisterDTO;
import com.cibershield.cibershield.dto.userDto.UserResponseDTO;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.service.jwt.JwtService;
import com.cibershield.cibershield.service.user.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public AuthResponseDTO register(@RequestBody UserRegisterDTO dto) {

        User user = userService.createUser(dto);  // usa DTO
        String token = jwtService.generateToken(user);

        UserResponseDTO userRes = new UserResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getUserRole().getRoleName()
        );

        return new AuthResponseDTO(token, userRes);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        UserResponseDTO userRes = new UserResponseDTO(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            user.getUserRole().getRoleName()
        );

        return new AuthResponseDTO(token, userRes);
    }
}
