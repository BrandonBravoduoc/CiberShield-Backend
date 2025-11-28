package com.cibershield.cibershield.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


    public User createUser(User user, String confirmPassword){
        if(user.getUserName() == null || user.getUserName().trim().isEmpty()){
            throw new RuntimeException("El nombre de usuario es obligatorio.");
        }
        if(userRepository.existsByUserName(user.getUserName())){
            throw new RuntimeException("El nombre de usuario no está disponible.");
        } 
        emailValidate(user.getEmail(),null);
        passwordValidate(user.getPassword(),confirmPassword);

        UserRole userRole = userRoleRepository.findByRoleName("CLIENTE");
        user.setUserRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public User userUpdate(Long id, String newUserName, String newEmail, String newPassword, String confirmPassword){
        User updateUser = userRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        if(newUserName != null && !newUserName.isBlank()){
            if(userRepository.existsByUserName(newUserName.trim())){
                throw new RuntimeException("El nombre de usuario no está disponible.");
            }
            updateUser.setUserName(newUserName.trim());
        }
        if(newEmail != null && !newEmail.isBlank()){
            emailValidate(newEmail,id);
            updateUser.setEmail(newEmail.trim().toLowerCase());
        }
        if(newPassword != null && !newPassword.isBlank()){
            passwordValidate(newPassword, confirmPassword);
            updateUser.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(updateUser);
    }

    public void changeMyPassword(Long userId, String currentPassword, 
                             String newPassword, String confirmNewPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        passwordValidate(newPassword, confirmNewPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
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
