package org.example.ecommerce.entity.admin;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminid;

    private String fullname;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String password;

    private String status = "active";

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "admin_roles",
            joinColumns = @JoinColumn(name = "adminid"),
            inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private List<AdminSession> sessions;

    @OneToMany(mappedBy = "admin")
    private List<SystemNotification> notifications;

    @OneToMany(mappedBy = "updatedBy")
    private List<ContentManagement> updatedContents;

    // Getters and setters
}

