package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

public record RecipeApplicability(
        String componentType,
        String machineModel,
        String position
) {
    public RecipeApplicability {
        if (componentType == null || componentType.isBlank()) {
            throw new IllegalArgumentException("validation.recipe-applicability.component-type.blank");
        }
        if (machineModel == null || machineModel.isBlank()) {
            throw new IllegalArgumentException("validation.recipe-applicability.machine-model.blank");
        }
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("validation.recipe-applicability.position.blank");
        }

        componentType = componentType.trim().toUpperCase();
        machineModel = machineModel.trim();
        position = position.trim();
    }
}