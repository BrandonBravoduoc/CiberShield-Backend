package com.cibershield.cibershield.service.user;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibershield.cibershield.dto.user.UserDTO;
import com.cibershield.cibershield.dto.user.UserDTO.UpdateUser;
import com.cibershield.cibershield.model.user.Address;
import com.cibershield.cibershield.model.user.Contact;
import com.cibershield.cibershield.model.user.User;
import com.cibershield.cibershield.model.user.UserRole;
import com.cibershield.cibershield.repository.user.ContactRepository;
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

    @Autowired
    private ContactRepository contactRepository;


    public UserDTO.Response createUser(UserDTO.Register dto){
        if(dto.userName() == null || dto.userName().trim().isEmpty()){
            throw new RuntimeException("El nombre de usuario es obligatorio.");
        }
        if(userRepository.existsByUserName(dto.userName())){
            throw new RuntimeException("El nombre de usuario no está disponible.");
        } 
        emailValidate(dto.email(),null);
        passwordValidate(dto.password(),dto.confirmPassword());

        User user = new User();
        user.setUserName(dto.userName());
        user.setImageUser(dto.imageUser());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));

        UserRole userRole = userRoleRepository.findByRoleName("CLIENTE")
            .orElseThrow(()-> new RuntimeException("Rol no encontrado."));
        user.setUserRole(userRole);

        user = userRepository.save(user);

        return new UserDTO.Response(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            user.getImageUser(),
            userRole.getRoleName()
        );
    }

    public List<UserDTO.Response> listUsers(){
        return userRepository.findAll().stream()    
            .map(user -> new UserDTO.Response(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getImageUser(),
                user.getUserRole().getRoleName()
            ))
            .toList();
    }

    public UserDTO.Response findByEmail(String email){
        User user = userRepository.findByEmail(email)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        return new UserDTO.Response(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            user.getImageUser(),
            user.getUserRole().getRoleName()
        );
    }

    public void delete(Long id){
        userRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado."));
        userRepository.deleteById(id);
    }


    @PreAuthorize("isAuthenticated()")
    public UserDTO.Response userUpdate(User currentUser, UpdateUser dto){
        if(dto.newUserName() != null && !dto.newUserName().isBlank()){
            if(userRepository.existsByUserName(dto.newUserName().trim())){
                throw new RuntimeException("El nombre de usuario no está disponible.");
            }
            currentUser.setUserName(dto.newUserName().trim());
        }
        if(dto.newEmail() != null && !dto.newEmail().isBlank()){
            emailValidate(dto.newEmail(),currentUser.getId());
            currentUser.setEmail(dto.newEmail().trim().toLowerCase());
        }


       User updateUser =  userRepository.save(currentUser);

       return new UserDTO.Response(
        updateUser.getId(),
        updateUser.getUserName(), 
        updateUser.getEmail(), 
        updateUser.getImageUser(),
        updateUser.getUserRole().getRoleName());
    }

    public void changeMyPassword(UserDTO.ChangePassword dto, Authentication auth) {
        User user = (User) auth.getPrincipal();
        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        passwordValidate(dto.newPassword(), dto.confirmPassword());
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
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


    public UserDTO.Profile myProfile(User currentUser) {

        Contact contact = contactRepository.findByUser(currentUser)
            .orElse(null);

      
        String addressInfo = null;
        if (contact != null && contact.getAddress() != null) {
            Address addr = contact.getAddress();
            addressInfo = addr.getStreet() + " " + addr.getNumber() +
                        ", " + addr.getCommune().getNameCommunity();
        }
        else{
            if(contact == null){
                addressInfo = "Sin información";
            }
        }

        return new UserDTO.Profile(
            currentUser.getUserName(),
            currentUser.getEmail(),
            contact != null ? contact.getName() : "Iompleta tu nombre",
            contact != null ? contact.getLastName() : "Ingresa tu apellido",
            contact != null ? contact.getPhone() : "Ingresa tu teléfono",
             addressInfo 
          
        );
    }



}
