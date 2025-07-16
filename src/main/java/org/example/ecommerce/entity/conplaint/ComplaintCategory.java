package org.example.ecommerce.entity.conplaint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "complaint_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintCategory {
    @Id
    @Column(name = "category_id")
    private Short categoryId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ComplaintReason> reasons;
}
