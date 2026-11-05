package com.innovacorp.reliant.platform.equipment.domain.model.commands;

import java.util.List;

public record RegisterHvofSubsystemCommand(
        Long hvofSystemId,
        String subsystemType,
        String name,
        String alias,
        List<String> parameters
) {
}