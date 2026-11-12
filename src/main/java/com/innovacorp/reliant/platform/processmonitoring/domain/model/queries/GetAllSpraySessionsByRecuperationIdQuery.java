package com.innovacorp.reliant.platform.processmonitoring.domain.model.queries;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;

public record GetAllSpraySessionsByRecuperationIdQuery(RecuperationId recuperationId) {
}