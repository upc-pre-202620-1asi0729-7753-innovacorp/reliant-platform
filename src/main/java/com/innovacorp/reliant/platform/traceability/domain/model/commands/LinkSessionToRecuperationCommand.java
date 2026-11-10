package com.innovacorp.reliant.platform.traceability.domain.model.commands;

public record LinkSessionToRecuperationCommand(Long recuperationId, Long sessionId) {
}