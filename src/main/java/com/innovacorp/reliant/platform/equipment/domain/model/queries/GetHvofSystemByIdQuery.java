package com.innovacorp.reliant.platform.equipment.domain.model.queries;

public record GetHvofSystemByIdQuery(Long id) {
    public GetHvofSystemByIdQuery {
        if (id == null || id <= 0) throw new IllegalArgumentException("hvof.system.error.id.invalid");
    }
}