package org.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Nationalized;


@Entity
@Table(name = "sellers")
@Data
public class Seller {
    @Id
    @Column(name = "sellerid", nullable = false)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "sellerid")

    private Shop shop;
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





    public Seller() {
    }
    public Seller(Integer id) {
        this.id = id;
    }
}