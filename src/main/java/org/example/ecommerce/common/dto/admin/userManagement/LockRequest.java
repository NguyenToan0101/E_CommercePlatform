package org.example.ecommerce.common.dto.admin.userManagement;

public class LockRequest {
    private Integer durationMinutes;

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
} 