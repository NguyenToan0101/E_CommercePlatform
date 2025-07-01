package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sellers")
@Getter
@Setter
public class Seller {
    @Id
    @Column(name = "sellerid", nullable = false)
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "sellerid")
    @MapsId
    private Customer customer;
    @Version
    private Integer version;
    @Size(max = 20)
    @Nationalized
    @Column(name = "idnumber", nullable = false, length = 20)
    private String idNumber;
    @Column(name = "frontidimage")
    private String frontIdImage; // base64 hoặc URL
    @Column(name = "backidimage")
    private String backIdImage;


    @OneToMany(mappedBy = "sellerid")
    private Set<Conversation> conversations = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "sellerid")
    @Nationalized
    private Shop shop;

    public Seller() {
    }
    public Seller(Integer id) {
        this.id = id;
    }
}