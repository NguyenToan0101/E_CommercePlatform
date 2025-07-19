package org.example.ecommerce.common.dto;

import jakarta.validation.constraints.NotNull;
import org.example.ecommerce.entity.Customer;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * DTO for {@link org.example.ecommerce.entity.admin.Activity}
 */
public record ActivityDto(Long id, @NotNull OffsetDateTime createdAt, String name, String action, String type,
                          String status) implements Serializable {
}