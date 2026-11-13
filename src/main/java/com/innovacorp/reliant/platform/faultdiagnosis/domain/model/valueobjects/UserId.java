package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects;

public record UserId(Long userId) {
    public UserId {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("User id must be positive");
    }
}
