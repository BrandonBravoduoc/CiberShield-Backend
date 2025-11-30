package com.cibershield.cibershield.controller.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cibershield.cibershield.dto.auth.AuthResponseDTO;
import com.cibershield.cibershield.dto.auth.LoginDTO;
import com.cibershield.cibershield.dto.user.UserDTO;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.service.jwt.JwtService;
import com.cibershield.cibershield.service.user.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


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
    private Cloudinary cloudinary;



    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestPart("userName") String userName,
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart("confirmPassword") String confirmPassword,
            @RequestPart(value = "imageUser", required = false) MultipartFile imageUser
    ) {
        try {

            String imageUrl = "https://res.cloudinary.com/dyf3i5iqa/image/upload/cibershield/default-avatar.png";

            if (imageUser != null && !imageUser.isEmpty()) {

                if (imageUser.getSize() > 8 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body("La imagen no puede pesar más de 8MB");
                }

                if (!imageUser.getContentType().startsWith("image/")) {
                    return ResponseEntity.badRequest().body("Solo se permiten imágenes");
                }

                Map<?, ?> result = cloudinary.uploader().upload(
                    imageUser.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "cibershield/users",
                            "transformation", ObjectUtils.asMap(
                                "width", 400, "height", 400, "crop", "limit",
                                "quality", "auto", "format", "auto"
                            )
                    )
                );
                imageUrl = (String) result.get("secure_url");
            }

            UserDTO.Register dto = new UserDTO.Register(
                    userName,
                    email,
                    password,
                    confirmPassword,
                    imageUrl
            );

            UserDTO.Response response = userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
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
