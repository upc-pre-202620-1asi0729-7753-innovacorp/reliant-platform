package com.innovacorp.reliant.platform.processmonitoring.domain.model.commands;

import java.time.Instant;

public record RegisterSprayPassCommand(Long spraySessionId, int passNumber, Instant startedAt, Instant endedAt) {
}
