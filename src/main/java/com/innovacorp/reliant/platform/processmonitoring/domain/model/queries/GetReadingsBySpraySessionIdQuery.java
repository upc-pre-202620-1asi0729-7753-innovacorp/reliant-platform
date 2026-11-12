package com.innovacorp.reliant.platform.processmonitoring.domain.model.queries;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;

/** A null band requests all readings for the session. */
public record GetReadingsBySpraySessionIdQuery(Long spraySessionId, Band band) {
}