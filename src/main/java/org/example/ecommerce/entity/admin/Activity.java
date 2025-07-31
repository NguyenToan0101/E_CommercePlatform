package org.example.ecommerce.entity.admin;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.ecommerce.entity.Customer;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "activities")

public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;



    private String name;

    @Column(name = "action", length = Integer.MAX_VALUE)
    private String action;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "status", length = Integer.MAX_VALUE)
    private String status;

    public Activity() {

    }

    public Activity(String name, String action, String type, String status) {
        this.name = name;
        this.action = action;
        this.type = type;
        this.status = status;
        this.createdAt = OffsetDateTime.now();
    }

    public enum Status{
        SUCCESS,
        WARNING,
        ERROR,
        INFO
    }

    public enum Type{
        user_register,
        product_report,
        order_complete,
        complaint,
        seller_register
    }
}