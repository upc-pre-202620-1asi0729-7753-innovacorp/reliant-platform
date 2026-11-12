package com.innovacorp.reliant.platform.processmonitoring.domain.model.queries;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;

/** band == null → all readings. */
public record GetReadingsBySpraySessionIdQuery(Long spraySessionId, Band band) {
}
