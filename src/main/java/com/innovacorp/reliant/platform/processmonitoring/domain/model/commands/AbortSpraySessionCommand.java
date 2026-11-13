package com.innovacorp.reliant.platform.processmonitoring.domain.model.commands;

public record AbortSpraySessionCommand(Long spraySessionId, String reason) {
}
