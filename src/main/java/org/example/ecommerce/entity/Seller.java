package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "idnumber")
    private String idnumber;

    @Column(name = "frontidimage")
    private String frontidimage;

    @Column(name = "backidimage")
    private String backidimage;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "sellerid")
    private Set<Conversation> conversations = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "sellerid")
    private Shop shop;

    public Seller() {
    }
    public Seller(Integer id) {
        this.id = id;
    }
}