package com.cibershield.cibershield.controller.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.service.jwt.JwtService;
import com.cibershield.cibershield.service.user.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private  UserService userService;

    @Autowired
    private  JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user, @RequestParam String confirmPassword) {
        User creado = userService.createUser(user, confirmPassword);
        String token = jwtService.generateToken(creado);

        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("user", creado);
        return res;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> creds) {
        String email = creds.get("email");
        String pass = creds.get("password");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(pass, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        Map<String, Object> res = new HashMap<>();
        res.put("token", jwtService.generateToken(user));
        res.put("user", user);
        return res;
    }

}
