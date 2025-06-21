package org.example.ecommerce.entity.admin;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleid;

    @Column(nullable = false, unique = true)
    private String rolename;

    private String description;

    private Integer level;



    @ManyToMany(mappedBy = "roles")
    private Set<Admin> admins;

    @ManyToMany
    @JoinTable(
            name = "authority_roles",
            joinColumns = @JoinColumn(name = "roleid"),
            inverseJoinColumns = @JoinColumn(name = "authorityid")
    )
    private Set<Authority> authorities;

    // Getters and setters
}

