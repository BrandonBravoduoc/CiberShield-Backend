package com.cibershield.cibershield.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Contact")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "id_address", nullable = true)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = true)
    private User user;

}