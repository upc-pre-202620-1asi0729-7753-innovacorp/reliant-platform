package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

public record RecipeApplicability(
        String componentType,
        String machineModel,
        String position
) {
    public RecipeApplicability {
        if (componentType == null || componentType.isBlank()) {
            throw new IllegalArgumentException("Component type is required");
        }
        if (machineModel == null || machineModel.isBlank()) {
            throw new IllegalArgumentException("Machine model is required");
        }
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Position is required");
        }

        componentType = componentType.trim().toUpperCase();
        machineModel = machineModel.trim();
        position = position.trim();
    }
}
