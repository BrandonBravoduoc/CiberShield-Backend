package com.cibershield.cibershield.service.user;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.userDto.UpdatePasswordDTO;
import com.cibershield.cibershield.dto.userDto.UserRegisterDTO;
import com.cibershield.cibershield.dto.userDto.UserResponseDTO;
import com.cibershield.cibershield.dto.userDto.UserUpdateDTO;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.model.user.UserRole;
import com.cibershield.cibershield.repository.user.UserRepository;
import com.cibershield.cibershield.repository.user.UserRoleRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private  UserRoleRepository userRoleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User createUser(UserRegisterDTO dto){
        if(dto.getUserName() == null || dto.getUserName().trim().isEmpty()){
            throw new RuntimeException("El nombre de usuario es obligatorio.");
        }
        if(userRepository.existsByUserName(dto.getUserName())){
            throw new RuntimeException("El nombre de usuario no está disponible.");
        } 
        emailValidate(dto.getEmail(),null);
        passwordValidate(dto.getPassword(),dto.getConfirmPassword());

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        UserRole userRole = userRoleRepository.findByRoleName("CLIENTE");
        user.setUserRole(userRole);
        return userRepository.save(user);
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
            .orElseThrow(()-> new RuntimeException("Correo no registrado."));
    }

    public void delete(Long id){
        userRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado."));
        userRepository.deleteById(id);
    }


    @PreAuthorize("isAuthenticated()")
    public UserResponseDTO userUpdate(User currentUser, UserUpdateDTO dto){
        if(dto.getNewUserName() != null && !dto.getNewUserName().isBlank()){
            if(userRepository.existsByUserName(dto.getNewUserName().trim())){
                throw new RuntimeException("El nombre de usuario no está disponible.");
            }
            currentUser.setUserName(dto.getNewUserName().trim());
        }
        if(dto.getNewEmail() != null && !dto.getNewEmail().isBlank()){
            emailValidate(dto.getNewEmail(),currentUser.getId());
            currentUser.setEmail(dto.getNewEmail().trim().toLowerCase());
        }
        if(dto.getNewPassword() != null && !dto.getConfirmPassword().isBlank()){
            passwordValidate(dto.getNewPassword(), dto.getConfirmPassword());
            currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

       User updateUser =  userRepository.save(currentUser);

       return new UserResponseDTO(
        updateUser.getId(),
        updateUser.getUserName(), 
        updateUser.getEmail(), 
        updateUser.getUserRole().getRoleName());
    }

    public void changeMyPassword(UpdatePasswordDTO dto, Authentication auth) {
        User user = userRepository.findById(dto.getId())
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        passwordValidate(dto.getNewPassword(), dto.getConfirmPassword());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public void emailValidate(String email, Long currentUserId) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("El correo es obligatorio.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (!normalizedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("El formato del correo no es válido.");
        }

        userRepository.findByEmail(normalizedEmail).ifPresent(existingUser -> {
            if (currentUserId == null || !existingUser.getId().equals(currentUserId)) {
                throw new RuntimeException("El correo ya está en uso.");
            }
        });
    }

    public void passwordValidate(String password, String confirmPassword){
        if(password == null || password.trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía.");
        }
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Las contraseñas no coinciden.");
        }
        if(password.length() < 8) {
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Debe contener al menos una mayúscula.");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("Debe contener al menos una minúscula.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new RuntimeException("Debe contener al menos un número.");
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            throw new RuntimeException("Debe contener al menos un carácter especial (@#$%^&+=!).");
        }
        if (password.contains(" ")) {
            throw new RuntimeException("No puede contener espacios.");
        }
    }




}
