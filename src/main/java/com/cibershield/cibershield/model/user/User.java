package com.cibershield.cibershield.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column (name ="image_user", columnDefinition = "TEXT", nullable = true )
    private String imageUser;

    @Column(name = "user_name",nullable = false, unique = true)
    private String userName;

    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "password",nullable = false)
    private String password;    

    @Column(name = "asset")
    private boolean asset = true;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Contact contact;
    
    @ManyToOne
    @JoinColumn(name = "id_rol_user", nullable = true)
    private UserRole userRole;

}