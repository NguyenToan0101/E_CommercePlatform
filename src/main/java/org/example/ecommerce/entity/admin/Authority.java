package org.example.ecommerce.entity.admin;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "authority")
@Data
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authorityid;

    private String name;

    private String description;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;

    // Getters and setters
}

