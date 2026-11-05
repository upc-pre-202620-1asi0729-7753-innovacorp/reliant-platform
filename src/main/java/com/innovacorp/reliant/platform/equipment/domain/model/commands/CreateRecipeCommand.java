package com.innovacorp.reliant.platform.equipment.domain.model.commands;

import java.util.List;

public record CreateRecipeCommand(
        Long hvofSystemId,
        Integer recipeNumber,
        String name,
        String powderSpecification,
        List<ApplicabilityItem> applicabilities,
        List<ParameterItem> parameters
) {
    public record ApplicabilityItem(
            String componentType,
            String machineModel,
            String position
    ) {}

    public record ParameterItem(
            String parameter,
            double setpoint,
            double lowerShutdown,
            double lowerWarning,
            double nominalMin,
            double nominalMax,
            double upperWarning,
            double upperShutdown,
            String unitSymbol,
            String unitCategory
    ) {}
}
