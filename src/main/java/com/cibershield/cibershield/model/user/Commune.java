package com.cibershield.cibershield.model.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "commune")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Commune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name_commune", nullable = false, unique = true)
    private String nameCommunity;

    @OneToMany(mappedBy = "street")
    @JsonIgnore
    private List<Address> address;

    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;
}