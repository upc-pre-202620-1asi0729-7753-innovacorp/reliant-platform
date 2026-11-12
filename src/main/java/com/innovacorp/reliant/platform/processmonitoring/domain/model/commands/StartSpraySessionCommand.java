package com.innovacorp.reliant.platform.processmonitoring.domain.model.commands;

public record StartSpraySessionCommand(
        Long hvofSystemId,
        Long recuperationId,
        Integer recipeNumber,
        String timeZone
) {
}